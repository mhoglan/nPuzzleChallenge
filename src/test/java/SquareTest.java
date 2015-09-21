import org.junit.Test;
import project.Square;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Michael Hoglan
 */
public class SquareTest {

        @Test
        public void checkSquares() {
            Square s1 = new Square("A", 1, 2, 3, 4);

            s1.rotate();

            assertArrayEquals(new int[]{4, 1, 2, 3}, s1.getEdges());

            s1.rotate(4);

            assertArrayEquals(new int[]{4, 1, 2, 3}, s1.getEdges());

            s1.rotate(1, 2, 3, 4);

            assertArrayEquals(new int[]{1, 2, 3, 4}, s1.getEdges());
        }
}