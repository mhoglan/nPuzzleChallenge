package project.resources;

/**
 * Created by michaelhoglan on 9/7/15.
 */
import com.codahale.metrics.annotation.Timed;
import project.Puzzle;
import project.PuzzleBank;
import project.PuzzleTracker;
import project.Square;
import project.utils.PuzzleTrackerPrinter;
import project.utils.PuzzleUtils;

import javax.imageio.ImageIO;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/puzzles")
@Produces(MediaType.APPLICATION_JSON)
public class PuzzleResource {
    @GET
    @Timed
    public List<Puzzle> getPuzzles() {
        return new ArrayList<>(PuzzleBank.getPuzzles().values());
    }

    @GET
    @Timed
    @Path("view")
    @Produces(MediaType.TEXT_PLAIN)
    public String viewPuzzles() {
        StringBuilder sb = new StringBuilder();

        sb.append("All Puzzles\n\n");
        for(Puzzle puzzle : PuzzleBank.getPuzzles().values()) {
            sb.append(puzzle.toString() + "\n");
        }

        return sb.toString();
    }

    @GET
    @Timed
    @Path("{puzzleId}")
    public Puzzle getPuzzle(@PathParam("puzzleId") String puzzleId) {
        Puzzle puzzle = PuzzleBank.getPuzzle(puzzleId);

        if(puzzle == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("puzzleId: " + puzzleId + " does not exist").build());
        }

        return puzzle;
    }

    @GET
    @Timed
    @Produces("text/plain")
    @Path("{puzzleId}/view")
    public String viewPuzzle(@PathParam("puzzleId") String puzzleId) {
        Puzzle puzzle = PuzzleBank.getPuzzle(puzzleId);

        if(puzzle == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("puzzleId: " + puzzleId + " does not exist").build());
        }

        return puzzle.toString();
    }

    @GET
    @Path("{puzzleId}/viewimage")
    @Produces("image/*")
    public Response viewPuzzleImage(@PathParam("puzzleId") String puzzleId, @DefaultValue("false") @QueryParam("label") Boolean label) throws IOException {
        Puzzle puzzle = PuzzleBank.getPuzzle(puzzleId);

        if (puzzle == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("puzzleId: " + puzzleId + " does not exist").build());
        }

        BufferedImage puzzleImage;
        try {
            puzzleImage = PuzzleUtils.generateImage(puzzle.getBoard(), label);
        } catch (IOException exception) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error generating image for puzzleId: " + puzzleId + "\n" + exception.getMessage()).build());
        }

        if (puzzleImage == null) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error generating image for puzzleId: " + puzzleId).build());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(puzzleImage, "png", baos);
        byte[] imageData = baos.toByteArray();

        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }

    @GET
    @Timed
    @Produces("text/plain")
    @Path("solve")
    public String solvePuzzles() {
        StringBuilder sb = new StringBuilder();

        sb.append("Solving All Puzzles\n\n");

        for(Puzzle puzzle : PuzzleBank.getPuzzles().values()) {
            sb.append("========================================\n\n");
            sb.append(puzzle.toString() + "\n");

            PuzzleTracker puzzleTracker = new PuzzleTracker();
            PuzzleBank.runPuzzle(puzzle.getPuzzleId(), puzzleTracker);
            sb.append(PuzzleTrackerPrinter.print(puzzleTracker) + "\n");
        }

        return sb.toString();
    }

    @GET
    @Timed
    @Produces("text/plain")
    @Path("{puzzleId}/solve")
    public String solvePuzzle(@PathParam("puzzleId") String puzzleId) {
        Puzzle puzzle = PuzzleBank.getPuzzle(puzzleId);

        if(puzzle == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("puzzleId: " + puzzleId + " does not exist").build());
        }

        StringBuilder sb = new StringBuilder();

        PuzzleTracker puzzleTracker = new PuzzleTracker();

        PuzzleBank.runPuzzle(puzzleId, puzzleTracker);

        sb.append("Solving Puzzle: " + puzzle.getPuzzleId() + "\n\n");
        sb.append(PuzzleTrackerPrinter.print(puzzleTracker));

        return sb.toString();
    }
}