package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private long id;
    private long booksInLibrary;
    private long signUpDays;
    private long booksShippingPerDay;
    private List<Book> books;
    private boolean scanned;

    private double score;

    public Library(long id, long booksInLibrary, long signUpDays, long booksShippingPerDay) {
        this.id = id;
        this.booksInLibrary = booksInLibrary;
        this.signUpDays = signUpDays;
        this.booksShippingPerDay = booksShippingPerDay;

        books = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSignUpDays() {
        return signUpDays;
    }

    public void setSignUpDays(long signUpDays) {
        this.signUpDays = signUpDays;
    }

    public long getBooksShippingPerDay() {
        return booksShippingPerDay;
    }

    public void setBooksShippingPerDay(long booksShippingPerDay) {
        this.booksShippingPerDay = booksShippingPerDay;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public long getBooksInLibrary() {
        return booksInLibrary;
    }

    public void setBooksInLibrary(long booksInLibrary) {
        this.booksInLibrary = booksInLibrary;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isScanned() {
        return scanned;
    }

    public boolean isNotScanned() {
        return !isScanned();
    }

    public void setScanned(boolean scanned){
        this.scanned = scanned;
    }
}
