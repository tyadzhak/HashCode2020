package org.example.model;

public class Book {
    private long id;
    private long score;
    private boolean scanned;

    public Book(long id, long score) {
        this.id = id;
        this.score = score;
        scanned = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public boolean isScanned() {
        return scanned;
    }

    public boolean isNotScanned(){
        return !isScanned();
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }
}
