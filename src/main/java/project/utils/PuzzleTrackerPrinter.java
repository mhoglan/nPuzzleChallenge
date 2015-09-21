package project.utils;

import project.PuzzleTracker;

/**
 * Created by michaelhoglan on 9/8/15.
 */
public class PuzzleTrackerPrinter {

    public static String print(PuzzleTracker puzzleTracker) {
        StringBuilder sb = new StringBuilder();

        sb.append("SUCCESS: " + puzzleTracker.getSuccessPuzzleStates().size() + "\n");
        for (int a = 0; a < puzzleTracker.getSuccessPuzzleStates().size(); a++) {
            sb.append("Solution " + (a + 1) + "\n");
            sb.append(puzzleTracker.getSuccessPuzzleStates().get(a).toString() + "\n");
        }

        sb.append("STATES ANALYZED: " + puzzleTracker.getStates() + "\n");
        sb.append("\n");

        return sb.toString();
    }
}
