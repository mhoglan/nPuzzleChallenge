package project;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by michaelhoglan on 9/8/15.
 */
public class Puzzle {

    private String puzzleId;
    private String description;
    private Set<Square> pieces = new LinkedHashSet<>();
    private PuzzleState board;
    private int rows;
    private int cols;

    public Puzzle(String puzzleId, String description, Set<Square> pieces) {
        this.puzzleId = puzzleId;
        this.description = description;
        this.pieces = pieces;

        this.rows = (int)Math.sqrt(pieces.size());
        this.cols = (int)Math.sqrt(pieces.size());

        this.board = new PuzzleState(this.rows, this.cols, this.pieces, this.puzzleId);

        for(Square piece : pieces) {
            this.board.place(piece.getId());
        }
    }

    public String getPuzzleId() {
        return puzzleId;
    }

    public String getDescription() {
        return description;
    }

    public Set<Square> getPieces() {
        return pieces;
    }

    public PuzzleState getBoard() {
        return board;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Puzzle: " + puzzleId + "\n");
        sb.append(description + "\n\n");
        sb.append("Board\n");
        sb.append(board.toString() + "\n");

        return sb.toString();
    }
}
