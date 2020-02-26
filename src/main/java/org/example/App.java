package org.example;

import org.example.model.Book;
import org.example.model.Library;
import org.example.model.Response;
import org.example.model.Scanning;
import org.example.strategy.StrategyProcessor;

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
        paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\f_libraries_of_the_world.txt"));
        paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\e_so_many_books.txt"));
        paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\d_tough_choices.txt"));
        paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\c_incunabula.txt"));
        paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\b_read_on.txt"));
        paths.add(Paths.get("C:\\Users\\taras_yadzhak\\Downloads\\a_example.txt"));
        StrategyProcessor processor = new StrategyProcessor();
        paths.forEach(path -> {
            try {
                //System.out.println("App::libraryScore");
                process(path, processor);


                //System.out.println("App::libraryScoreTest");
                //process(path, App::libraryScoreTest);

                System.out.println("---------------------------------------------------------");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void process(Path path, StrategyProcessor processor)
            throws IOException {
        List<String> strings = Files.readAllLines(path, StandardCharsets.UTF_8);

        Scanning scanning = readScanning(strings);
        Map<Integer, Book> books = collectBooks(strings);
        List<Library> libraries = collectLibraries(strings, books, scanning, processor);

        Response response = process(path, scanning, libraries, processor);
        //printOutput(response, path);
    }

    private static Response process(Path path, Scanning scanning, List<Library> libraries, StrategyProcessor processor) {
        List<Library> sortedLibrary = processor.getSortedLibrary(scanning, libraries);

        Response response = new Response();
        long i = scanning.getTotalDays();
        long allScore = 0;
        while (i >= 0) {


            Library library = sortedLibrary.get(0);
            if (library.getSignUpDays() > scanning.getDaysLeft()) {
                library.setScanned(true);
                sortedLibrary.remove(0);
                break;
            }

            scanning.setDaysLeft(scanning.getDaysLeft() - library.getSignUpDays());
            long possibleAmountOfBookThatCanBeShipped =
                    scanning.getDaysLeft() * library.getBooksShippingPerDay();

            List<Book> collect = library.getBooks().stream().filter(Book::isNotScanned)
                    .sorted(Comparator.comparing(Book::getScore).reversed())
                    .collect(Collectors.toList());

            boolean addLibrary = false;
            for (int k = 0; k < possibleAmountOfBookThatCanBeShipped; k++) {
                if (collect.isEmpty())
                    break;

                Book book = collect.get(0);
                book.setScanned(true);
                List<Long> booksIds = response.getBookIds().get(library.getId());
                if (booksIds == null) {
                    booksIds = new ArrayList<>();

                }

                addLibrary = true;
                allScore += book.getScore();
                booksIds.add(book.getId());
                response.getBookIds().put(library.getId(), booksIds);
                collect.remove(0);
            }

            if (addLibrary)
                response.getLibraryIds().add(library.getId());

            i = i - library.getSignUpDays();
            library.setScanned(true);
            sortedLibrary.remove(0);
            if (sortedLibrary.isEmpty()) {
                sortedLibrary = processor.getSortedLibrary(scanning, libraries);
            }

            calculateLibrariesScore(sortedLibrary, scanning, processor);
        }
        System.out.println(path.getFileName() + " all score " + allScore);
        return response;
    }

    private static void calculateLibrariesScore(List<Library> libraries, Scanning s,
            StrategyProcessor processor) {
        libraries.forEach(library -> {
            library.setScore(processor.libraryScoreWithBookScore(library, s));
        });
    }

    private static List<Library> collectLibraries(List<String> strings, Map<Integer, Book> books,
            Scanning scanning, StrategyProcessor processor) {

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

            library.setScore(processor.libraryScore(library, scanning));
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

        for (long lId : response.getLibraryIds()) {
            List<Long> bookIds = response.getBookIds().get(lId);
            String libStr = String.format("%d %d", lId, bookIds.size());
            lines.add(libStr);

            String bookStr = bookIds.stream().map(integer -> integer.toString())
                    .collect(Collectors.joining(" "));

            lines.add(bookStr);
        }

        Files.write(file, lines);

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
        scanning.setDaysLeft(scanning.getTotalDays());

        return scanning;
    }
}
