import org.junit.Test;
import project.Square;
import project.utils.SquareChecker;
import project.SquareEdgeOrientation;

import static org.junit.Assert.*;

/**
 * @author Michael Hoglan
 */
public class SquareCheckerTest {

        @Test
        public void checkSquares() {
            SquareChecker checker = new SquareChecker();

            Square s1 = new Square("A", 1, 2, 3, 4);


            // Check equal squares
            Square s2 = new Square("B", 1, 2, 3, 4);
            assertTrue(SquareChecker.equal(s1, s2));
            assertTrue(SquareChecker.equal(s1, s2, true));
            assertTrue(SquareChecker.equal(s1, s2, false));

            // Check non equal squares
            Square s3 = new Square("C", 5, 2, 3, 4);
            assertFalse(SquareChecker.equal(s1, s3));
            assertFalse(SquareChecker.equal(s1, s3, true));
            assertFalse(SquareChecker.equal(s1, s3, false));

            // Check squares that match when rotated
            Square s4 = new Square("D", 4, 1, 2, 3);
            assertFalse(SquareChecker.equal(s1, s4));
            assertTrue(SquareChecker.equal(s1, s4, true));
            assertFalse(SquareChecker.equal(s1, s4, false));

        }
        @Test
        public void checkEdges() {
            Square s1 = new Square("A", 1, 2, 3, 4);

            // Check specific edge
            assertTrue(SquareChecker.containsEdge(s1, 1));
            assertTrue(SquareChecker.containsEdge(s1, 2));
            assertTrue(SquareChecker.containsEdge(s1, 3));
            assertTrue(SquareChecker.containsEdge(s1, 4));

            // Check wildcard edge
            assertTrue(SquareChecker.containsEdge(s1, -1));

            // Check wrong edge
            assertFalse(SquareChecker.containsEdge(s1, 5));

            // Check specific edge with specific orientation
            assertTrue(SquareChecker.containsEdge(s1, 1, SquareEdgeOrientation.N));
            assertTrue(SquareChecker.containsEdge(s1, 2, SquareEdgeOrientation.E));
            assertTrue(SquareChecker.containsEdge(s1, 3, SquareEdgeOrientation.S));
            assertTrue(SquareChecker.containsEdge(s1, 4, SquareEdgeOrientation.W));

            // Check wrong orientation
            assertFalse(SquareChecker.containsEdge(s1, 1, SquareEdgeOrientation.E));
            assertFalse(SquareChecker.containsEdge(s1, 1, SquareEdgeOrientation.W));
            assertFalse(SquareChecker.containsEdge(s1, 1, SquareEdgeOrientation.S));

            // Check wrong edge at specific orientation
            assertFalse(SquareChecker.containsEdge(s1, 5, SquareEdgeOrientation.N));
            assertFalse(SquareChecker.containsEdge(s1, 5, SquareEdgeOrientation.E));
            assertFalse(SquareChecker.containsEdge(s1, 5, SquareEdgeOrientation.W));
            assertFalse(SquareChecker.containsEdge(s1, 5, SquareEdgeOrientation.S));

            // Check with no rotation
            assertTrue(SquareChecker.containsEdges(s1, new int[]{1, 2, 3, 4}));
            assertTrue(SquareChecker.containsEdges(s1, new int[]{1, 2, 3, 4}, true));
            assertTrue(SquareChecker.containsEdges(s1, new int[]{1, 2, 3, 4}, false));

            // Check with List vs int[]
            assertTrue(SquareChecker.containsEdges(s1, SquareChecker.createListFromArray(new int[]{1, 2, 3, 4})));
            assertTrue(SquareChecker.containsEdges(s1, SquareChecker.createListFromArray(new int[]{1, 2, 3, 4}), true));
            assertTrue(SquareChecker.containsEdges(s1, SquareChecker.createListFromArray(new int[]{1, 2, 3, 4}), false));

            // Check wrong edge list
            assertFalse(SquareChecker.containsEdges(s1, new int[]{5, 2, 3, 4}));
            assertFalse(SquareChecker.containsEdges(s1, new int[]{5, 2, 3, 4}, true));
            assertFalse(SquareChecker.containsEdges(s1, new int[]{5, 2, 3, 4}, false));

            // Check too many edges
            assertFalse(SquareChecker.containsEdges(s1, new int[]{1, 2, 3, 4, 5}));
            assertFalse(SquareChecker.containsEdges(s1, new int[]{1, 2, 3, 4, 5}, true));
            assertFalse(SquareChecker.containsEdges(s1, new int[]{1, 2, 3, 4, 5}, false));

            // Check with rotation
            assertFalse(SquareChecker.containsEdges(s1, new int[]{4, 1, 2, 3}));
            assertTrue(SquareChecker.containsEdges(s1, new int[]{4, 1, 2, 3}, true));
            assertFalse(SquareChecker.containsEdges(s1, new int[]{4, 1, 2, 3}, false));

            // Check with rotation too many edges
            assertFalse(SquareChecker.containsEdges(s1, new int[]{4, 1, 2, 3, 5}));
            assertFalse(SquareChecker.containsEdges(s1, new int[]{4, 1, 2, 3, 5}, true));
            assertFalse(SquareChecker.containsEdges(s1, new int[]{4, 1, 2, 3, 5}, false));

            // Check with wildcard mask
            assertTrue(SquareChecker.matchesMask(s1, new int[]{1, -1, -1, -1}));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{1, -1, -1, -1}, true));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{1, -1, -1, -1}, false));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{1, 2, -1, -1}));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{1, 2, 3, -1}));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{1, -1, 3, -1}));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{4, -1, -1, -1}, true));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{4, 1, -1, -1}, true));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{4, 1, 2, -1}, true));
            assertFalse(SquareChecker.matchesMask(s1, new int[]{4, -1, -1, -1}, false));
            assertTrue(SquareChecker.matchesMask(s1, new int[]{4, -1, 2, -1}, true));
            assertFalse(SquareChecker.matchesMask(s1, new int[]{4, -1, 3, -1}, true));

            // Check with List vs int[]
            assertTrue(SquareChecker.matchesMask(s1, SquareChecker.createListFromArray(new int[]{1, -1, -1, -1})));
            assertTrue(SquareChecker.matchesMask(s1, SquareChecker.createListFromArray(new int[]{1, -1, -1, -1}), true));
            assertTrue(SquareChecker.matchesMask(s1, SquareChecker.createListFromArray(new int[]{1, -1, -1, -1}), false));
            assertFalse(SquareChecker.matchesMask(s1, SquareChecker.createListFromArray(new int[]{4, -1, -1, -1})));
            assertTrue(SquareChecker.matchesMask(s1, SquareChecker.createListFromArray(new int[]{4, -1, -1, -1}), true));
            assertFalse(SquareChecker.matchesMask(s1, SquareChecker.createListFromArray(new int[]{4, -1, -1, -1}), false));
        }

}
