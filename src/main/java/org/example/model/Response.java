package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {

    private List<Integer> libraryIds = new ArrayList<>();

    //key  library id , value book id
    private Map<Integer, List<Integer>> bookIds = new HashMap<>();

    public List<Integer> getLibraryIds() {
        return libraryIds;
    }

    public void setLibraryIds(List<Integer> libraryIds) {
        this.libraryIds = libraryIds;
    }

    public Map<Integer, List<Integer>> getBookIds() {
        return bookIds;
    }

    public void setBookIds(Map<Integer, List<Integer>> bookIds) {
        this.bookIds = bookIds;
    }
}
