package project.utils;

import project.Square;
import project.SquareEdgeOrientation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael Hoglan
 */
public class SquareChecker {

    public static Integer EDGE_WILDCARD = -1;

    public static boolean equal(Square s1, Square s2) {
        return equal(s1, s2, false);
    }

    public static boolean equal(Square s1, Square s2, boolean allowRotation) {
        return containsEdges(s1, s2.getEdges(), allowRotation);
    }

    public static boolean containsEdge(Square s, int edge) {
        return containsEdge(s, edge, SquareEdgeOrientation.WILDCARD);
    }

    public static boolean containsEdge(Square s, int edge, SquareEdgeOrientation orientation) {
        if(edge == EDGE_WILDCARD) {
            return true;
        }

        List<Integer> edgeList = createListFromArray(s.getEdges());

        if(orientation == SquareEdgeOrientation.WILDCARD) {
            return edgeList.contains(edge);
        }
        else {
            return edgeList.get(orientation.toInt()) == edge;
        }
    }

    public static boolean containsEdges(Square s, int[] edges) {
        return containsEdges(s, edges, false);
    }

    public static boolean containsEdges(Square s, int[] edges, boolean allowRotation) {
        return containsEdges(s, createListFromArray(edges), allowRotation);
    }


    public static boolean containsEdges(Square s, List<Integer> edges) {
        return containsEdges(s, edges, false);
    }

    public static boolean containsEdges(Square s, List<Integer> edges, boolean allowRotation) {
        // Just use the edges as a mask with no wildcards
        // More efficient to implement here, but cost of mask comparison is small
        return matchesMask(s, edges, allowRotation);
    }

    public static boolean matchesMask(Square s, int[] mask) {
        return matchesMask(s, mask, false);
    }

    public static boolean matchesMask(Square s, int[] mask, boolean allowRotation) {
        return matchesMask(s, createListFromArray(mask), allowRotation);
    }

    public static boolean matchesMask(Square s, List<Integer> mask) {
        return matchesMask(s, mask, false);
    }

    public static boolean matchesMask(Square s, List<Integer> mask, boolean allowRotation) {
        int rotations = matchesMaskReturnRotations(s, mask);

        // negative is returned if mask can't match being rotated
        if (rotations <= -1) {
            return false;
        }
        else {
            if (!allowRotation) {
                // 0 rotations if allowRotation is not allowed
                return (rotations == 0);
            } else {
                // 0 or more rotations if allowRotation is allowed
                return rotations >= 0;
            }
        }
    }

    public static void applyMaskWildcards(List<Integer> edgeList, List<Integer> maskList) {
        for (int i = 0; i < maskList.size(); i++) {
            if (maskList.get(i).equals(EDGE_WILDCARD)) {
                edgeList.set(i, EDGE_WILDCARD);
            }
        }
    }

    public static int matchesMaskReturnRotations(Square s, List<Integer> maskList) {
        List<Integer> edgeList;

        // Try rotating the list up to size times and checking equal
        for (int r = 0; r < maskList.size(); r++) {
            // Reset edgeList since cleared from wildcards earlier
            edgeList = createListFromArray(s.getEdges());

            // Rotate the edgeList so we can return number of rotations required
            Collections.rotate(edgeList, r);

            // Clear out any edges which are wildcard (-1) to allow comparison
            applyMaskWildcards(edgeList, maskList);

            if (edgeList.equals(maskList)) {
                return r;
            }
        }
        // TODO probably should be an exception or change to Integer and have null return
        return -1;
    }

    // Needed because autoboxing to ArrayList from primitive arrays is Java 8
    public static List<Integer> createListFromArray(int[] array) {
        List<Integer> integerList = new ArrayList<>(array.length);

        for(int a : array) {
            integerList.add(a);
        }

        return integerList;
    }
}