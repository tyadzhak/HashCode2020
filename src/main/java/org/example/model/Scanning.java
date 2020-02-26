package org.example.model;

public class Scanning {

    private long totalBooks;
    private long totalLibrary;
    private long totalDays;
    private long daysLeft;

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public long getTotalLibrary() {
        return totalLibrary;
    }

    public void setTotalLibrary(long totalLibrary) {
        this.totalLibrary = totalLibrary;
    }

    public long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(long totalDays) {
        this.totalDays = totalDays;
    }

    public long getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }
}
