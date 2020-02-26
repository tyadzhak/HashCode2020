package org.example.strategy;

import org.example.model.Book;
import org.example.model.Library;
import org.example.model.Scanning;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestStrategy extends BaseStrategy {

    @Override
    public double libraryScore(Library l, Scanning s) {
        return l.getBooksShippingPerDay() * l.getBooksInLibrary() * (s.getDaysLeft() / l.getSignUpDays());
    }

    @Override
    public double libraryScoreWithBookScore(Library l, Scanning s) {
        int booksScore = 0;
        long amountOfNotScannedBookInLibrary = 0;
        for (Book book : l.getBooks()) {

            if (book.isScanned())
                continue;

            amountOfNotScannedBookInLibrary++;
            booksScore += book.getScore();
        }
        if(s.getDaysLeft() == 0){
int i = 0;
        }

        if (s.getDaysLeft() != 0)
            return /*booksScore **/ ((l.getBooksShippingPerDay() * amountOfNotScannedBookInLibrary)
                    / (s.getDaysLeft() / l.getSignUpDays()));
        else
            return /*booksScore **/ ((l.getBooksShippingPerDay() * amountOfNotScannedBookInLibrary)
                    / (l.getSignUpDays()));
    }

    @Override
    public List<Library> getSortedLibrary(Scanning scanning, List<Library> libraries) {
        List<Library> collect = libraries.stream().filter(Library::isNotScanned)
                .sorted(Comparator.comparing(Library::getSignUpDays)).collect(Collectors.toList());
        return collect.isEmpty() ? libraries : collect;
    }
}
