package org.example.strategy;

import org.example.model.Library;
import org.example.model.Scanning;

import java.util.List;

public interface Strategy {
    //Response process();
    double libraryScore(Library l, Scanning s);
    double libraryScoreWithBookScore(Library l, Scanning s);
    List<Library> getSortedLibrary(Scanning scanning, List<Library> libraries);
}
