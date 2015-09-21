package project.utils;

import project.PuzzleEvaluateReport;
import project.PuzzleState;
import project.Square;
import project.SquareEdgeOrientation;

/**
 * Created by michaelhoglan on 9/8/15.
 */
public class PuzzleEvaluator {

    public static PuzzleEvaluateReport evaluate(PuzzleState puzzleState) {
        PuzzleEvaluateReport report = new PuzzleEvaluateReport();

        report.setUnusedPiecesCount(puzzleState.getUnusedPieces().size());
        report.setUsedPiecesCount(puzzleState.getUsedPieces().size());

        Square firstSquare = puzzleState.get(0, 0);


        report.setIsFirstSquareA(firstSquare != null && firstSquare.getId().equalsIgnoreCase("A"));

        report.setIsFirstEdge1(firstSquare != null && firstSquare.getEdge(SquareEdgeOrientation.N) == 1);

        int overallSquareConflicts = 0;
        int overallEdgeConflicts = 0;
        int overallMissingSquares = 0;

        for (int x = 0; x < puzzleState.getRows(); x++) {
            for (int y = 0; y < puzzleState.getCols(); y++) {
                int edgeConflicts = 0;

                Square s = null;
                Square s0 = null;
                Square s1 = null;
                Square s2 = null;
                Square s3 = null;

                s = puzzleState.get(x, y);

                if (s == null) {
                    overallMissingSquares++;
                    continue;
                } else {
                    s0 = puzzleState.get(x - 1, y);
                    s1 = puzzleState.get(x, y + 1);
                    s2 = puzzleState.get(x + 1, y);
                    s3 = puzzleState.get(x, y - 1);


                    if (s0 != null) {
                        if (s.getEdge(SquareEdgeOrientation.N) != s0.getEdge(SquareEdgeOrientation.S))
                            edgeConflicts++;
                    }

                    if (s1 != null) {
                        if (s.getEdge(SquareEdgeOrientation.E) != s1.getEdge(SquareEdgeOrientation.W))
                            edgeConflicts++;
                    }

                    if (s2 != null) {
                        if (s.getEdge(SquareEdgeOrientation.S) != s2.getEdge(SquareEdgeOrientation.N))
                            edgeConflicts++;
                    }

                    if (s3 != null) {
                        if (s.getEdge(SquareEdgeOrientation.W) != s3.getEdge(SquareEdgeOrientation.E))
                            edgeConflicts++;
                    }

                    overallEdgeConflicts += edgeConflicts;

                    if (edgeConflicts > 0) {
                        overallSquareConflicts++;
                    }
                }
            }
        }

        report.setEdgeConflictsCount(overallEdgeConflicts);
        report.setSquareConflictsCount(overallSquareConflicts);

        if(overallEdgeConflicts + overallMissingSquares + overallSquareConflicts == 0 && report.isFirstSquareA() && report.isFirstEdge1()) {
            report.setSolved(true);
        }

        return report;
    }
}
