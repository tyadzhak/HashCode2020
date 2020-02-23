package org.example.model;

public class Book {
    private int id;
    private int score;
    private boolean scanned;

    public Book(int id, int score) {
        this.id = id;
        this.score = score;
        scanned = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
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
