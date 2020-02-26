package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {

    private List<Long> libraryIds = new ArrayList<>();

    //key  library id , value book id
    private Map<Long, List<Long>> bookIds = new HashMap<>();

    public List<Long> getLibraryIds() {
        return libraryIds;
    }

    public void setLibraryIds(List<Long> libraryIds) {
        this.libraryIds = libraryIds;
    }

    public Map<Long, List<Long>> getBookIds() {
        return bookIds;
    }

    public void setBookIds(Map<Long, List<Long>> bookIds) {
        this.bookIds = bookIds;
    }
}
