package org.example.strategy;

import org.example.model.Library;
import org.example.model.Scanning;

import java.util.List;

public class StrategyProcessor implements Strategy{
    private DataSetCStrategy dataSetCStrategy = new DataSetCStrategy();
    private DataSetFStrategy dataSetFStrategy = new DataSetFStrategy();
    private TestStrategy testStrategy = new TestStrategy();

    @Override
    public double libraryScore(Library l, Scanning s) {
        return calculateScore(dataSetCStrategy.libraryScore(l, s),
                dataSetFStrategy.libraryScore(l, s), testStrategy.libraryScore(l, s));
    }

    @Override
    public double libraryScoreWithBookScore(Library l, Scanning s) {
        return calculateScore(dataSetCStrategy.libraryScoreWithBookScore(l, s),
                dataSetFStrategy.libraryScoreWithBookScore(l, s), testStrategy.libraryScoreWithBookScore(l, s));
    }

    @Override
    public List<Library> getSortedLibrary(Scanning scanning, List<Library> libraries) {
        return dataSetFStrategy.getSortedLibrary(scanning, libraries);
    }

    private double calculateScore(double v, double v2, double v3) {
        return Math.max(Math.max(v, v2), v3);
    }
}
