package project;

import project.utils.PuzzleEvaluator;
import project.utils.PuzzleTrackerPrinter;
import project.utils.PuzzleUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author Michael Hoglan
 */
public class PuzzleBank {
    private static Map<String, Puzzle> puzzles = new HashMap<>();

    static {
        Puzzle puzzle;

        puzzle = new Puzzle("100", "1 Puzzle", new LinkedHashSet<>(Arrays.asList(
                new Square("A", 4, 1, 2, 3))));

        puzzles.put(puzzle.getPuzzleId(), puzzle);

        puzzle = new Puzzle("200", "4 Puzzle (Rotation Only)", new LinkedHashSet<>(Arrays.asList(
                new Square("A", 4, 1, 2, 3),
                new Square("B", 7, 2, 5, 6),
                new Square("C", 3, 8, 9, 10),
                new Square("D", 11, 12, 8, 7))));

        puzzles.put(puzzle.getPuzzleId(), puzzle);

        puzzle = new Puzzle("201", "4 Puzzle (Move Only)", new LinkedHashSet<>(Arrays.asList(
                new Square("C", 3, 8, 9, 10),
                new Square("D", 7, 11, 12, 8),
                new Square("B", 5, 6, 7, 2),
                new Square("A", 1, 2, 3, 4))));

        puzzles.put(puzzle.getPuzzleId(), puzzle);

        puzzle = new Puzzle("202", "4 Puzzle (Move and Rotate)", new LinkedHashSet<>(Arrays.asList(
                new Square("C", 12, 8, 6, 7),
                new Square("A", 10, 2, 11, 1),
                new Square("B", 9, 5, 2, 6),
                new Square("D", 10, 4, 3, 7))));


        puzzles.put(puzzle.getPuzzleId(), puzzle);

        puzzle = new Puzzle("300", "9 Puzzle (Move and Rotate)", new LinkedHashSet<>(Arrays.asList(
                new Square("I", 15, 18, 17, 6),
                new Square("D", 16, 5, 8, 4),
                new Square("C", 24, 21, 8, 19),
                new Square("F", 3, 22, 23, 13),
                new Square("E", 12, 15, 22, 11),
                new Square("B", 4, 9, 23, 6),
                new Square("A", 16, 17, 2, 1),
                new Square("G", 21, 10, 7, 9),
                new Square("H", 13, 7, 14, 20))));

        puzzles.put(puzzle.getPuzzleId(), puzzle);

        puzzle = new Puzzle("1301", "9 Puzzle Repeating Edges (Move and Rotate)", new LinkedHashSet<>(Arrays.asList(
                new Square("A", 1, 4, 6, 3),
                new Square("D", 1, 4, 5, 2),
                new Square("C", 3, 5, 2, 1),
                new Square("E", 3, 1, 6, 2),
                new Square("F", 1, 2, 5, 3),
                new Square("H", 2, 6, 5, 1),
                new Square("I", 5, 2, 3, 6),
                new Square("B", 5, 1, 4, 1),
                new Square("G", 6, 4, 5, 4))));

        puzzles.put(puzzle.getPuzzleId(), puzzle);

        puzzle = new Puzzle("500", "16 Puzzle (Move and Rotate)", new LinkedHashSet<>(Arrays.asList(
                new Square("U", 36, 39, 16, 31),
                new Square("F", 43, 2, 24, 36),
                new Square("Q", 47, 39, 19, 55),
                new Square("Y", 57, 30, 34, 42),
                new Square("D", 38, 53, 34, 54),
                new Square("O", 40, 50, 43, 18),
                new Square("A", 1, 11, 50, 13),
                new Square("R", 20, 12, 42, 51),
                new Square("M", 12, 19, 24, 9),
                new Square("N", 32, 51, 53, 49),
                new Square("X", 35, 22, 57, 9),
                new Square("L", 58, 14, 27, 60),
                new Square("W", 2, 11, 8, 35),
                new Square("V", 7, 28, 55, 20),
                new Square("H", 7, 32, 25, 59),
                new Square("I", 60, 5, 18, 31),
                new Square("T", 58, 16, 6, 37),
                new Square("C", 47, 41, 10, 6),
                new Square("J", 28, 48, 15, 41),
                new Square("G", 48, 59, 33, 3),
                new Square("B", 4, 14, 23, 29),
                new Square("S", 37, 45, 21, 23),
                new Square("E", 44, 45, 10, 52),
                new Square("K", 15, 46, 17, 52),
                new Square("P", 26, 56, 46, 3))));

        puzzles.put(puzzle.getPuzzleId(), puzzle);
    }

    private static Set<Square> shufflePuzzle(Set<Square> pieces,int seed) {
        Set<String> originalIdSet = new LinkedHashSet<>();
        Set<Integer> originalEdgeSet = new LinkedHashSet<>();

        for(Square piece: pieces) {
            originalIdSet.add(piece.getId());
            originalEdgeSet.add(piece.getEdge(SquareEdgeOrientation.N));
            originalEdgeSet.add(piece.getEdge(SquareEdgeOrientation.E));
            originalEdgeSet.add(piece.getEdge(SquareEdgeOrientation.S));
            originalEdgeSet.add(piece.getEdge(SquareEdgeOrientation.W));
        }

        List<String> originalIdList = new ArrayList<>(originalIdSet);
        List<Integer> originalEdgeList = new ArrayList<>(originalEdgeSet);

        List<String> shuffleIdList = new ArrayList<>(originalIdList);
        List<Integer> shuffleEdgeList = new ArrayList<>(originalEdgeList);

        Collections.shuffle(shuffleIdList, new Random(seed));
        Collections.shuffle(shuffleEdgeList, new Random(seed));

        // Put 1st Square back at first position
        // To enforce A needs to be [0.0]
        Collections.swap(shuffleIdList, 0, shuffleIdList.indexOf(originalIdList.get(0)));
        Collections.swap(shuffleEdgeList, originalEdgeList.indexOf(1), shuffleEdgeList.indexOf(1));
        Set<Square> newPieces = new LinkedHashSet<>();

        for(Square piece : pieces) {
            newPieces.add(Square.build(shuffleIdList.get(originalIdList.indexOf(piece.getId())),
                    shuffleEdgeList.get(originalEdgeList.indexOf(piece.getEdge(SquareEdgeOrientation.N))),
                    shuffleEdgeList.get(originalEdgeList.indexOf(piece.getEdge(SquareEdgeOrientation.E))),
                    shuffleEdgeList.get(originalEdgeList.indexOf(piece.getEdge(SquareEdgeOrientation.S))),
                    shuffleEdgeList.get(originalEdgeList.indexOf(piece.getEdge(SquareEdgeOrientation.W)))).originalId(piece.getOriginalId()));
        }

        return newPieces;
    }


    public static void runAllPuzzles(PuzzleTracker puzzleTracker) {
        for(String puzzleId: puzzles.keySet()) {
            runPuzzle(puzzleId, puzzleTracker);
        }
    }

    public static void runPuzzle(String puzzleId, PuzzleTracker puzzleTracker) {
        Set<Square> pieces = puzzles.get(puzzleId).getPieces();

        PuzzleState originalPuzzle = new PuzzleState((int) Math.sqrt(pieces.size()), (int) Math.sqrt(pieces.size()), pieces, puzzleId);

        for(Square piece : pieces) {
            originalPuzzle.place(piece.getId());
        }

        // Iterate through and create the different ways to start
        // the puzzle with a square in the top left corner.  There
        // are N * 4 ways to start the puzzle.
        for (Square s : pieces) {
            for (int r = 0; r < 4; r++) {
                Square startingPiece = new Square(s);
                startingPiece.rotate(r);

                PuzzleState startingPuzzle = new PuzzleState((int) Math.sqrt(pieces.size()), (int) Math.sqrt(pieces.size()), pieces, puzzleId);
                startingPuzzle.get(s.getId()).rotate(r);
                startingPuzzle.place(s.getId());

                PuzzleWorker puzzleWorker = new PuzzleWorker(startingPuzzle, puzzleTracker);

                // false will have the tree create all possible states and find
                // all solutions.
                // true will have it stop on the first solution encountered.
                // Goes by depth first search.

                puzzleTracker.incrementStates();

                if (PuzzleEvaluator.evaluate(startingPuzzle).isSolved()) {
                    puzzleTracker.addSuccess(startingPuzzle);
                }
                else {
                    puzzleWorker.run(false);
                }
            }
        }
    }


    public static Map<String, Puzzle> getPuzzles() {
        return puzzles;
    }

    public static Puzzle getPuzzle(String puzzleId) {
        return puzzles.get(puzzleId);
    }

    public static void main(String[] args) {

        PuzzleTracker puzzleTracker = new PuzzleTracker();

        PuzzleBank.runAllPuzzles(puzzleTracker);

        System.out.println(PuzzleTrackerPrinter.print(puzzleTracker));
    }
}