package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private int id;
    private int booksInLibrary;
    private int signUpDays;
    private int booksShippingPerDay;
    private List<Book> books;
    private double score;

    public Library(int id, int booksInLibrary, int signUpDays, int booksShippingPerDay) {
        this.id = id;
        this.booksInLibrary = booksInLibrary;
        this.signUpDays = signUpDays;
        this.booksShippingPerDay = booksShippingPerDay;

        books = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSignUpDays() {
        return signUpDays;
    }

    public void setSignUpDays(int signUpDays) {
        this.signUpDays = signUpDays;
    }

    public int getBooksShippingPerDay() {
        return booksShippingPerDay;
    }

    public void setBooksShippingPerDay(int booksShippingPerDay) {
        this.booksShippingPerDay = booksShippingPerDay;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getBooksInLibrary() {
        return booksInLibrary;
    }

    public void setBooksInLibrary(int booksInLibrary) {
        this.booksInLibrary = booksInLibrary;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
