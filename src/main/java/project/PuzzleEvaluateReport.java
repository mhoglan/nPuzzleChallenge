package project;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by michaelhoglan on 9/8/15.
 */
public class PuzzleEvaluateReport {

    @JsonProperty
    private int unusedPiecesCount = 0;

    @JsonProperty
    private int usedPiecesCount = 0;

    @JsonProperty
    private int edgeConflictsCount = 0;

    @JsonProperty
    private int squareConflictsCount = 0;

    @JsonProperty
    private boolean isFirstSquareA = false;

    @JsonProperty
    private boolean isFirstEdge1 = false;

    @JsonProperty
    private boolean solved = false;

    public int getUnusedPiecesCount() {
        return unusedPiecesCount;
    }

    public void setUnusedPiecesCount(int unusedPiecesCount) {
        this.unusedPiecesCount = unusedPiecesCount;
    }

    public int getUsedPiecesCount() {
        return usedPiecesCount;
    }

    public void setUsedPiecesCount(int usedPiecesCount) {
        this.usedPiecesCount = usedPiecesCount;
    }

    public int getEdgeConflictsCount() {
        return edgeConflictsCount;
    }

    public void setEdgeConflictsCount(int edgeConflictsCount) {
        this.edgeConflictsCount = edgeConflictsCount;
    }

    public int getSquareConflictsCount() {
        return squareConflictsCount;
    }

    public void setSquareConflictsCount(int squareConflictsCount) {
        this.squareConflictsCount = squareConflictsCount;
    }

    public boolean isFirstSquareA() {
        return isFirstSquareA;
    }

    public void setIsFirstSquareA(boolean isFirstSquareA) {
        this.isFirstSquareA = isFirstSquareA;
    }

    public boolean isFirstEdge1() {
        return isFirstEdge1;
    }

    public void setIsFirstEdge1(boolean isFirstEdge1) {
        this.isFirstEdge1 = isFirstEdge1;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
