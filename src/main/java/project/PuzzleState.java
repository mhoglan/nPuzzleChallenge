package project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Michael Hoglan
 */
public class PuzzleState {

    @JsonProperty
    private Square[][] board = null;

    @JsonProperty
    private int rows;

    @JsonProperty
    private int cols;

    @JsonProperty
    private String puzzleId;

    // Use LinkedHashMap to have deterministic ordering based on insertion
    @JsonProperty
    private Map<String, Square> pieces = new LinkedHashMap<>();

    @JsonProperty
    private Map<String, Square> usedPieces = new LinkedHashMap<>();

    public PuzzleState(int rows, int cols, Set<Square> pieces, String puzzleId) {
        this.rows = rows;
        this.cols = cols;
        this.puzzleId = puzzleId;
        this.board = new Square[rows][cols];

        for(Square[] row : this.board) {
            Arrays.fill(row, null);
        }

        for(Square piece : pieces) {
            this.pieces.put(piece.getId(), piece.newCopy());
        }
    }

    public PuzzleState(PuzzleState puzzleState) {
        this.rows = puzzleState.rows;
        this.cols = puzzleState.cols;
        this.puzzleId = puzzleState.puzzleId;
        this.board = new Square[rows][cols];

        for(Square[] row : this.board) {
            Arrays.fill(row, null);
        }

        // Get a deep copy of pieces
        for(Map.Entry<String, Square> entry : puzzleState.pieces.entrySet()) {
            Square squareCopy = entry.getValue().newCopy();

            this.pieces.put(squareCopy.getId(), squareCopy);
        }

        // Regenerate board by placing pieces that exist
        // Will generate the usedPieces while placing
        for(int x = 0; x < this.rows; x++) {
            for(int y = 0; y < this.cols; y++) {
                Square piece = puzzleState.get(x, y);

                // No piece at that position
                if(piece == null) {
                    continue;
                }

                // Place piece by ID in same position
                place(piece.getId(), x, y);
            }
        }
    }

    public PuzzleState newCopy() {
        return new PuzzleState(this);
    }

    public boolean place(String squareId, int x, int y) {
        Square piece = pieces.get(squareId);

        if (piece == null) {
            return false;
        }

        if (usedPieces.containsKey(squareId)) {
            return false;
        }

        Square temp = board[x][y];

        if(temp != null) {
            remove(x,y);
        }

        board[x][y] = piece;
        usedPieces.put(piece.getId(), piece);

        return true;
    }

    public boolean place(String squareId) {
        boolean result = place(squareId, getCurrentXPosition(), getCurrentYPosition());

        return result;
    }

    @JsonIgnore
    public Square get(int x, int y) {
        Square temp = null;
        try {
            temp = board[x][y];
        } catch (Exception e) {
            temp = null;
        }

        return temp;
    }

    public boolean exists(int x, int y) {
        return get(x,y) != null;
    }

    public Square get(int i) {
        Square temp = null;
        try {
            temp = board[i / rows][i % cols];
        } catch (Exception e) {
            temp = null;
        }

        return temp;
    }

    @JsonIgnore
    public Square get(String squareId) {
        return pieces.get(squareId);
    }

    public Set<Square> getUsedPieces() {
        return new LinkedHashSet<>(usedPieces.values());
    }

    public Set<Square> getPieces() {
        return new LinkedHashSet<>(pieces.values());
    }

    public Set<Square> getUnusedPieces() {
        Set<Square> unusedPieces = new LinkedHashSet<>(pieces.values());
        unusedPieces.removeAll(usedPieces.values());

        return unusedPieces;
    }

    @JsonIgnore
    public int getCurrentXPosition() {
        for(int x = 0; x < rows; x++) {
            for(int y = 0; y < cols; y++) {
                if(get(x,y) == null) {
                    return x;
                }
            }
        }

        return -1;
    }

    @JsonIgnore
    public int getCurrentYPosition() {
        for(int x = 0; x < rows; x++) {
            for(int y = 0; y < cols; y++) {
                if(get(x,y) == null) {
                    return y;
                }
            }
        }

        return -1;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String getPuzzleId() {
        return puzzleId;
    }

    @JsonIgnore
    public int getXPosition(String squareId) {
        for(int x = 0; x < rows; x++) {
            for(int y = 0; y < cols; y++) {
                Square square = get(x,y);

                if(square != null && square.getId().equalsIgnoreCase(squareId)) {
                    return x;
                }
            }
        }

        return -1;
    }

    @JsonIgnore
    public int getYPosition(String squareId) {
        for(int x = 0; x < rows; x++) {
            for(int y = 0; y < cols; y++) {
                Square square = get(x,y);

                if(square != null && square.getId().equalsIgnoreCase(squareId)) {
                    return y;
                }
            }
        }

        return -1;
    }

    public void swap(int x1, int y1, int x2, int y2) {
        Square temp = null;
        temp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = temp;
    }

    public void remove(int x1, int y1) {
        Square square = get(x1, y1);

        if(square != null) {
            board[x1][y1] = null;
            usedPieces.remove(square.getId());
        }
    }

    public void clear() {
        usedPieces.clear();

        board = new Square[rows][cols];

        for(Square[] row : this.board) {
            Arrays.fill(row, null);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                sb.append("----------------");
            }

            sb.append("-\n");

            for (int y = 0; y < cols; y++) {
                Square piece = board[x][y];
                formatter.format("|       %-4s    ", piece == null ? "" : piece.getEdge(SquareEdgeOrientation.N));
            }

            sb.append("|\n");

            for (int y = 0; y < cols; y++) {
                sb.append("|               ");
            }

            sb.append("|\n");

            for (int y = 0; y < cols; y++) {
                Square piece = board[x][y];

                if(piece == null) {
                    formatter.format("|%s", StringUtils.center("[" + x + " " + y + "]", 15));
//                    formatter.format("|   %-4s%-4s%-4s", piece == null ? "" : piece.getEdge(SquareEdgeOrientation.W), piece == null ? "[" + x + " " + y + "]" : piece.getId(), piece == null ? "" : piece.getEdge(SquareEdgeOrientation.E));
                }
                else {
                    formatter.format("|   %-4s%-4s%-4s", piece == null ? "" : piece.getEdge(SquareEdgeOrientation.W), piece == null ? "[" + x + " " + y + "]" : piece.getId(), piece == null ? "" : piece.getEdge(SquareEdgeOrientation.E));
                }

            }

            sb.append("|\n");


            for (int y = 0; y < cols; y++) {
                sb.append("|               ");
            }

            sb.append("|\n");

            for (int y = 0; y < cols; y++) {
                Square piece = board[x][y];
                formatter.format("|       %-4s    ", piece == null ? "" : piece.getEdge(SquareEdgeOrientation.S));
            }

            sb.append("|\n");
        }

        for (int y = 0; y < cols; y++) {
            sb.append("----------------");
        }

        sb.append("-\n");

        return sb.toString();
    }
}