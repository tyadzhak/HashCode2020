package org.example;

import org.example.model.Book;
import org.example.model.Library;
import org.example.model.Response;
import org.example.model.Scanning;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        List<Path> paths = new ArrayList<>();
        //paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\f_libraries_of_the_world.txt"));
        //paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\e_so_many_books.txt"));
        //paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\d_tough_choices.txt"));
        paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\c_incunabula.txt"));
        //paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\b_read_on.txt"));
        //paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\a_example.txt"));
        paths.forEach(path -> {
            try {
                System.out.println("App::libraryScore");
                process(path, App::libraryScoreC);


                //System.out.println("App::libraryScoreTest");
                //process(path, App::libraryScoreTest);

                System.out.println("---------------------------------------------------------");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void process(Path path, BiFunction<Library, Scanning, Double> libraryScore)
            throws IOException {
        List<String> strings = Files.readAllLines(path, StandardCharsets.UTF_8);

        Scanning scanning = readScanning(strings);
        Map<Integer, Book> books = collectBooks(strings);
        List<Library> libraries = collectLibraries(strings, books, scanning, libraryScore);

        Response response = process(path, scanning, libraries);

        //printOutput(response, path);


    }

    private static Response process(Path path, Scanning scanning, List<Library> libraries) {
        List<Library> sortedLibrary = getSortedLibrary2(scanning, libraries);

        Response response = new Response();
        int i = scanning.getTotalDays();
        int allScore = 0;
        while (i >= 0) {


            Library library = sortedLibrary.get(0);
            if (library.getSignUpDays() > i)
                break;

            int possibleAmountOfBookThatCanBeShipped =
                    (scanning.getTotalDays() - library.getSignUpDays()) * library
                            .getBooksShippingPerDay();

            List<Book> collect = library.getBooks().stream().filter(Book::isNotScanned)
                    .sorted(Comparator.comparing(Book::getScore).reversed())
                    .collect(Collectors.toList());

            boolean addLibrary = false;
            for (int k = 0; k < possibleAmountOfBookThatCanBeShipped; k++) {
                if (collect.size() <= k)
                    break;

                Book book = collect.get(k);
                book.setScanned(true);
                List<Integer> booksIds = response.getBookIds().get(library.getId());
                if (booksIds == null) {
                    booksIds = new ArrayList<>();

                }

                addLibrary = true;
                allScore += book.getScore();
                booksIds.add(book.getId());
                response.getBookIds().put(library.getId(), booksIds);

            }

            if (addLibrary)
                response.getLibraryIds().add(library.getId());

            i = i - library.getSignUpDays();
            sortedLibrary.remove(0);
            if (sortedLibrary.isEmpty())
                break;

            calculateLibrariesScore(sortedLibrary, scanning);
            sortedLibrary = getSortedLibrary2(scanning, sortedLibrary);
        }
        System.out.println(path.getFileName() + " all score " + allScore);
        return response;
    }

    private static void calculateLibrariesScore(List<Library> libraries, Scanning s) {
        libraries.forEach(library -> {
            library.setScore(libraryScoreWithBookScoreC(library, s));
        });
    }

    private static List<Library> collectLibraries(List<String> strings, Map<Integer, Book> books,
            Scanning scanning, BiFunction<Library, Scanning, Double> libraryScore) {

        List<Library> libraries = new ArrayList<>();

        int libraryIndex = 0;
        for (int i = 2; i < strings.size(); i = i + 2) {
            String libraryStr = strings.get(i);

            if (libraryStr == null || libraryStr.trim().isEmpty()) {
                continue;
            }

            String[] librarySplittedStr = libraryStr.split(" ");

            String signUpDays = librarySplittedStr[1];
            Library library = new Library(libraryIndex, Integer.parseInt(librarySplittedStr[0]),
                    Integer.parseInt(signUpDays), Integer.parseInt(librarySplittedStr[2]));

            String booksInLibraryStr = strings.get(i + 1);
            Arrays.stream(booksInLibraryStr.split(" ")).forEach(s -> {
                int bookId = Integer.parseInt(s);
                Book book = books.get(bookId);
                library.getBooks().add(book);
            });

            library.setScore(libraryScore.apply(library, scanning));
            libraries.add(library);
            libraryIndex++;
        }

        return libraries;
    }

    private static void printOutput(Response response, Path path) throws IOException {
        Path output = path.getParent().resolve(path.getFileName() + "_output.txt");
        Path file = Files.createFile(output);

        String firstLine = String.format("%s", response.getLibraryIds().size());
        List<String> lines = new ArrayList<>();
        lines.add(firstLine);

        for (Integer lId : response.getLibraryIds()) {
            List<Integer> bookIds = response.getBookIds().get(lId);
            String libStr = String.format("%d %d", lId, bookIds.size());
            lines.add(libStr);

            String bookStr = bookIds.stream().map(integer -> integer.toString())
                    .collect(Collectors.joining(" "));

            lines.add(bookStr);
        }

        Files.write(file, lines);

    }

    private static double libraryScore(Library l, Scanning s) {
        return l.getBooksShippingPerDay() * l.getBooksInLibrary() / l.getSignUpDays();
    }

    private static double libraryScoreC(Library l, Scanning s) {
        return l.getBooksShippingPerDay() * l.getBooksInLibrary() * (s.getTotalDays() / l.getSignUpDays());
    }

    private static double libraryScoreWithBookScore(Library l, Scanning s) {
        int booksScore = 0;
        int amountOfNotScannedBookInLibrary = 0;
        for (Book book : l.getBooks()) {

            if (book.isScanned())
                continue;

            amountOfNotScannedBookInLibrary++;
            booksScore += book.getScore();
        }

        return booksScore * ((l.getBooksShippingPerDay() * amountOfNotScannedBookInLibrary) / l
                .getSignUpDays());
    }

    private static double libraryScoreWithBookScoreC(Library l, Scanning s) {
        int booksScore = 0;
        int amountOfNotScannedBookInLibrary = 0;
        for (Book book : l.getBooks()) {

            if (book.isScanned())
                continue;

            amountOfNotScannedBookInLibrary++;
            booksScore += book.getScore();
        }

        return /*booksScore **/ ((l.getBooksShippingPerDay() * amountOfNotScannedBookInLibrary) /
                (l.getSignUpDays()));
    }

    private static List<Library> getSortedLibrary1(Scanning scanning, List<Library> libraries) {
        List<Library> collect =
                libraries.stream().sorted(Comparator.comparing(Library::getSignUpDays))
                        .collect(Collectors.toList());
        return collect.isEmpty() ? libraries : collect;
    }

    private static List<Library> getSortedLibrary2(Scanning scanning, List<Library> libraries) {
        List<Library> collect =
                libraries.stream().sorted(Comparator.comparing(Library::getScore).reversed())
                        .collect(Collectors.toList());
        return collect.isEmpty() ? libraries : collect;
    }

    private static Map<Integer, Book> collectBooks(List<String> strings) {
        Map<Integer, Book> books = new HashMap<>();
        String str = strings.get(1);
        String[] scoresStr = str.split(" ");


        for (int i = 0; i < scoresStr.length; i++) {
            books.put(i, new Book(i, Integer.parseInt(scoresStr[i])));
        }
        return books;
    }

    private static Scanning readScanning(List<String> strings) {
        Scanning scanning = new Scanning();
        String s = strings.get(0);
        String[] scanningStr = s.split(" ");
        scanning.setTotalBooks(Integer.parseInt(scanningStr[0]));
        scanning.setTotalLibrary(Integer.parseInt(scanningStr[1]));
        scanning.setTotalDays(Integer.parseInt(scanningStr[2]));

        return scanning;
    }
}
