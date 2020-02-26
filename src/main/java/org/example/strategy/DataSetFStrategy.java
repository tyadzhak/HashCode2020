package org.example.strategy;

import org.example.model.Book;
import org.example.model.Library;
import org.example.model.Scanning;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DataSetFStrategy extends BaseStrategy {

    @Override
    public double libraryScore(Library l, Scanning s) {
        return l.getBooksShippingPerDay() * l.getBooksInLibrary() / l.getSignUpDays();
    }

    @Override
    public double libraryScoreWithBookScore(Library l, Scanning s) {
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

    public List<Library> getSortedLibrary(Scanning scanning, List<Library> libraries) {
        List<Library> collect =
                libraries.stream().sorted(Comparator.comparing(Library::getScore).reversed())
                        .collect(Collectors.toList());
        return collect.isEmpty() ? libraries : collect;
    }
}
