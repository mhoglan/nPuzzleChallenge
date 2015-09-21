package project.utils;

import project.Puzzle;
import project.PuzzleBank;
import project.PuzzleState;
import project.Square;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Michael Hoglan
 */
public class PuzzleUtils {

    public static List<String> alphaList = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    public static List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            17, 18, 19, 20 ,21, 22, 23, 24, 25, 26, 27, 28, 29, 30);

    // Needed because autoboxing to ArrayList from primitive arrays is Java 8
    public static List<Integer> createListFromArray(int[] array) {
        List<Integer> integerList = new ArrayList<>(array.length);

        for(int a : array) {
            integerList.add(a);
        }

        return integerList;
    }

    public static BufferedImage generateImage(PuzzleState puzzle, boolean label) throws IOException {
        int rows = puzzle.getRows();
        int cols = puzzle.getCols();

        int chunks = rows * cols;

        BufferedImage[] puzzleImages = new BufferedImage[chunks];

        for(int x = 0; x < rows; x++) {
            for(int y = 0; y < cols; y++) {
                int index = (x * rows) + y;
                Square piece = puzzle.get(x, y);

                if(piece == null) {
                    puzzleImages[index] = ImageIO.read(PuzzleUtils.class.getResourceAsStream("/blank.png"));

                    if(label) {
                        labelImage(puzzleImages[index], "-", new int[]{-1, -1, -1, -1}, x, y);
                    }
                }
                else {
//                    int r = SquareChecker.matchesMaskReturnRotations(piece, PuzzleUtils.createListFromArray(PuzzleBank.getPuzzle(puzzle.getPuzzleId()).getBoard().get(piece.getId()).getEdges()));
                    int r = piece.getRotations();

                    puzzleImages[index] = rotate(ImageIO.read(PuzzleUtils.class.getResourceAsStream("/" + puzzle.getPuzzleId() + "/" + piece.getOriginalId() +  ".png")), 90 * r);

                    if(label) {
                        labelImage(puzzleImages[index], piece.getId(), piece.getEdges(), x, y);
                    }
                }
            }
        }

        return combineImages(puzzleImages, rows, cols);
    }

    private static BufferedImage rotate(BufferedImage image, double angle) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(angle), image.getWidth() / 2, image.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    private static BufferedImage combineImages(BufferedImage[] images, int rows, int cols) {
        int type = images[0].getType();
        int chunkWidth = images[0].getWidth();
        int chunkHeight = images[0].getHeight();

        //Initializing the final image
        BufferedImage finalImg = new BufferedImage(chunkWidth*cols, chunkHeight*rows, type);

        int num = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                finalImg.createGraphics().drawImage(images[num], chunkWidth * j, chunkHeight * i, null);
                num++;
            }
        }

        return finalImg;
    }
    private static void labelImage(BufferedImage image, String squareId, int[] edges, int x, int y) {
        Graphics2D graphics = image.createGraphics();

        if(image.getHeight() < 200) {
            graphics.setFont(graphics.getFont().deriveFont(25.0f));
        }
        else {
            graphics.setFont(graphics.getFont().deriveFont(40.0f));
        }

        int actualWidth;
        int actualHeight;
        int xPadding = 25;
        int yPadding = 25;

        graphics.setColor(Color.MAGENTA);

        // Draw ID
        actualWidth = graphics.getFontMetrics().stringWidth(squareId);
        actualHeight = graphics.getFontMetrics().getHeight();
        graphics.drawString(squareId,
                (image.getWidth() + actualWidth) / 2 - actualWidth,
                ((image.getHeight() + actualHeight) / 2) - (actualHeight / 2));

        // Draw coordinates
        actualWidth = graphics.getFontMetrics().stringWidth("[" + x + "," + y + "]");
        actualHeight = graphics.getFontMetrics().getHeight();
        graphics.drawString("[" + x + "," + y + "]",
                (image.getWidth() + actualWidth) / 2 - actualWidth,
                ((image.getHeight() + actualHeight) / 2) + (actualHeight / 2));

        // Draw N edge
        actualWidth = graphics.getFontMetrics().stringWidth(Integer.toString(edges[0]));
        actualHeight = graphics.getFontMetrics().getHeight();

        graphics.drawString(Integer.toString(edges[0]),
                (image.getWidth() + actualWidth) / 2 - actualWidth,
                actualHeight);

        // Draw W edge
        actualWidth = graphics.getFontMetrics().stringWidth(Integer.toString(edges[3]));
        actualHeight = graphics.getFontMetrics().getHeight();

        graphics.drawString(Integer.toString(edges[3]),
                xPadding,
                (image.getHeight() + actualHeight) / 2);

        // Draw E edge
        actualWidth = graphics.getFontMetrics().stringWidth(Integer.toString(edges[1]));
        actualHeight = graphics.getFontMetrics().getHeight();

        graphics.drawString(Integer.toString(edges[1]),
                image.getWidth() - actualWidth - xPadding,
                (image.getHeight() + actualHeight) / 2);

        // Draw S edge
        actualWidth = graphics.getFontMetrics().stringWidth(Integer.toString(edges[2]));
        actualHeight = graphics.getFontMetrics().getHeight();

        graphics.drawString(Integer.toString(edges[2]),
                (image.getWidth() + actualWidth) / 2 - actualWidth,
                image.getHeight() - yPadding);
    }
}
