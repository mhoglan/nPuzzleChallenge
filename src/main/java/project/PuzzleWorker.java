package project;

/**
 * @author Michael Hoglan
 */
public class PuzzleWorker {

    private PuzzleState puzzleState = null;
    private PuzzleTracker puzzleTracker = null;

    public PuzzleWorker(PuzzleState puzzleState, PuzzleTracker puzzleTracker) {
        this.puzzleState = puzzleState;
        this.puzzleTracker = puzzleTracker;
    }

    public void run(boolean stopOnFirst) {
       // TODO: For the user to implement
    }

    @Override
    public String toString() {
        return puzzleState.toString();
    }
}