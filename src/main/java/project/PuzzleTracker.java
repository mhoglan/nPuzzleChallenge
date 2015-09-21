package project;

import project.utils.PuzzleTrackerPrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Hoglan
 */
public class PuzzleTracker {
    private boolean stopAll = false;
    private int states = 0;
    private List<PuzzleWorker> puzzleWorkers = new ArrayList<>();
    private List<PuzzleState> successPuzzleStates = new ArrayList<>();

    public PuzzleTracker() {

    }

    public void stopAll() {
        stopAll = true;
    }

    public boolean isStopAll() {
        return stopAll;
    }

    public void addSuccess(PuzzleState puzzleState) {
        successPuzzleStates.add(puzzleState.newCopy());
    }

    public void addWorker(PuzzleWorker puzzleWorker) {
        puzzleWorkers.add(puzzleWorker);
    }

    public void incrementStates() {
        states++;
    }

    public int getStates() {
        return states;
    }

    public List<PuzzleWorker> getPuzzleWorkers() {
        return puzzleWorkers;
    }

    public List<PuzzleState> getSuccessPuzzleStates() {
        return successPuzzleStates;
    }

    public String toString() {
        return PuzzleTrackerPrinter.print(this);
    }
}
