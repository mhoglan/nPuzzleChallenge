package project.resources;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import project.datastore.ChallengeDatastore;
import project.Puzzle;
import project.PuzzleEvaluateReport;
import project.utils.PuzzleEvaluator;
import project.PuzzleBank;
import project.PuzzleState;
import project.Square;
import project.utils.PuzzleUtils;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Created by michaelhoglan on 9/8/15.
 */
@Path("/challenges")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class ChallengeResource {
    private ChallengeDatastore datastore = new ChallengeDatastore();

    @POST
    @Timed
    public Response startPuzzle(@FormParam("puzzleId") String puzzleId) {
        Puzzle puzzle = PuzzleBank.getPuzzle(puzzleId);

        if(puzzle == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("puzzleId: " + puzzleId + " does not exist").build();
        }

        PuzzleState challenge = puzzle.getBoard().newCopy();

        Integer challengeId = datastore.insertElement(challenge);

        return Response.created(UriBuilder.fromResource(ChallengeResource.class).path(challengeId.toString()).build()).build();
    }

    @POST
    @Path("{challengeId}/action/clone")
    @Timed
    public Response clonePuzzle(@PathParam("challengeId") Integer challengeId) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        PuzzleState challengeCopy = challenge.newCopy();

        Integer challengeCopyId = datastore.insertElement(challengeCopy);

        return Response.created(UriBuilder.fromResource(ChallengeResource.class).path(challengeCopyId.toString()).build()).build();
    }

    @GET
    @Path("{challengeId}")
    @Timed
    public PuzzleState getChallenge(@PathParam("challengeId") Integer challengeId) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        return challenge;
    }

    @GET
    @Path("{challengeId}/view")
    @Timed
    @Produces("text/plain")
    public String viewChallenge(@PathParam("challengeId") Integer challengeId) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        return viewChallenge(challenge);
    }

    @GET
    @Path("{challengeId}/viewimage")
    @Produces("image/*")
    public Response viewPuzzleImage(@PathParam("challengeId") Integer challengeId, @DefaultValue("false") @QueryParam("label") Boolean label) throws IOException {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        BufferedImage puzzleImage;
        try {
            puzzleImage = PuzzleUtils.generateImage(challenge, label);
        } catch (IOException exception) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error generating image for challengeId: " + challengeId + "\n" + exception.getMessage()).build());
        }

        if (puzzleImage == null) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error generating image for challengeId: " + challengeId).build());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(puzzleImage, "png", baos);
        byte[] imageData = baos.toByteArray();

        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }

    @DELETE
    @Timed
    @Path("{challengeId}")
    public String removeChallenge(@PathParam("challengeId") Integer challengeId) {
        PuzzleState challenge = datastore.removeElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        datastore.removeElement(challengeId);

        return viewChallenge(challenge);
    }

    private String viewChallenge(PuzzleState challenge) {
        StringBuilder sb = new StringBuilder();

        sb.append("Board\n");
        sb.append(challenge.toString());
        sb.append("\n");

        Set<Square> unusedPieces = challenge.getUnusedPieces();

        sb.append("Unused Pieces: " + unusedPieces.size() + "\n");

        for(Square piece : unusedPieces) {
            sb.append(piece.toString() + "\n");
        }

        return sb.toString();
    }

    @POST
    @Timed
    @Path("{challengeId}/action/rotate")
    public Response rotate(@PathParam("challengeId") Integer challengeId,
                                @FormParam("x1") Integer x1,
                                @FormParam("y1") Integer y1,
                                @FormParam("squareId1") String squareId1) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        if(StringUtils.isNotBlank(squareId1)) {
            x1 = challenge.getXPosition(squareId1);
            y1 = challenge.getYPosition(squareId1);
        }

        if(x1 == null || x1 >= challenge.getRows() || x1 < 0 || y1 == null || y1 >= challenge.getCols() || y1 < 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("invalid coordinates [ " + x1 + " " + y1 + " ]").build());
        }

        Square piece = challenge.get(x1, y1);


        if(piece == null) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("no piece was found at [ " + x1 + " " + y1 + "]").build());
        }

        piece.rotate();

        return Response.ok().build();
    }

    @POST
    @Timed
    @Path("{challengeId}/action/swap")
    public Response swap(@PathParam("challengeId") Integer challengeId,
                                @FormParam("x1") Integer x1,
                                @FormParam("y1") Integer y1,
                                @FormParam("x2") Integer x2,
                                @FormParam("y2") Integer y2,
                                @FormParam("squareId1") String squareId1,
                                @FormParam("squareId2") String squareId2) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        if(StringUtils.isNotBlank(squareId1)) {
            x1 = challenge.getXPosition(squareId1);
            y1 = challenge.getYPosition(squareId1);
        }

        if(StringUtils.isNotBlank(squareId2)) {
            x2 = challenge.getXPosition(squareId2);
            y2 = challenge.getYPosition(squareId2);
        }

        if(x1 == null || x1 >= challenge.getRows() || x1 < 0 || y1 == null || y1 >= challenge.getCols() || y1 < 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("invalid coordinates [ " + x1 + " " + y1 + " ]").build());
        }

        if(x2 == null || x2 >= challenge.getRows() || x2 < 0 || y2 == null || y2 >= challenge.getCols() || y2 < 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("invalid coordinates [ " + x2 + " " + y2 + " ]").build());
        }

        challenge.swap(x1, y1, x2, y2);

        return Response.ok().build();
    }

    @POST
    @Timed
    @Path("{challengeId}/action/move")
    public Response move(@PathParam("challengeId") Integer challengeId,
                         @FormParam("x1") Integer x1,
                         @FormParam("y1") Integer y1,
                         @FormParam("x2") Integer x2,
                         @FormParam("y2") Integer y2,
                         @FormParam("squareId1") String squareId1,
                         @FormParam("squareId2") String squareId2,
                         @FormParam("direction") String direction) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        if(StringUtils.isNotBlank(squareId1)) {
            x1 = challenge.getXPosition(squareId1);
            y1 = challenge.getYPosition(squareId1);
        }

        if(StringUtils.isNotBlank(direction)) {
            switch (direction.toLowerCase()) {
                case "up":
                    x2 = x1 - 1;
                    y2 = y1;
                    break;
                case "down":
                    x2 = x1 + 1;
                    y2 = y1;
                    break;
                case "right":
                    x2 = x1;
                    y2 = y1 + 1;
                    break;
                case "left":
                    x2 = x1;
                    y2 = y1 - 1;
                    break;
                default:
                    throw new WebApplicationException(Response.status(400).entity("invalid direction: " + direction).build());
            }
        }

        if(StringUtils.isNotBlank(squareId2)) {
            x2 = challenge.getXPosition(squareId2);
            y2 = challenge.getYPosition(squareId2);
        }

        if(x1 == null || x1 >= challenge.getRows() || x1 < 0 || y1 == null || y1 >= challenge.getCols() || y1 < 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("invalid coordinates [ " + x1 + " " + y1 + " ]").build());
        }


        if(x2 == null || x2 >= challenge.getRows() || x2 < 0 || y2 == null || y2 >= challenge.getCols() || y2 < 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("invalid coordinates [ " + x2 + " " + y2 + " ]").build());
        }

        int delta = 0;

        delta += Math.abs(x2 - x1) + Math.abs(y2 - y1);

        if(delta > 1) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("cannot move more than one space (delta: " + delta + ") requested: [ " + x1 + " " + y1 + " ] -> [ " + x2 + " " + y2 + " ]").build());
        }

        challenge.swap(x1, y1, x2, y2);

        return Response.ok().build();
    }

    @POST
    @Timed
    @Path("{challengeId}/action/remove")
    public Response remove(@PathParam("challengeId") Integer challengeId,
                           @FormParam("x1") Integer x1,
                           @FormParam("y1") Integer y1,
                           @FormParam("squareId1") String squareId1) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        if(StringUtils.isNotBlank(squareId1)) {
            x1 = challenge.getXPosition(squareId1);
            y1 = challenge.getYPosition(squareId1);
        }

        if(x1 == null || x1 >= challenge.getRows() || x1 < 0 || y1 == null || y1 >= challenge.getCols() || y1 < 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("invalid coordinates [ " + x1 + " " + y1 + " ]").build());
        }

        challenge.remove(x1, y1);

        return Response.ok().build();
    }

    @POST
    @Timed
    @Path("{challengeId}/action/place")
    public Response place(@PathParam("challengeId") Integer challengeId,
                          @FormParam("squareId1") String squareId1,
                          @FormParam("x1") Integer x1,
                          @FormParam("y1") Integer y1) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        if(x1 == null) {
            x1 = challenge.getCurrentXPosition();
        }

        if(y1 == null) {
            y1 = challenge.getCurrentYPosition();
        }

        if(x1 == null || x1 >= challenge.getRows() || x1 < 0 || y1 == null || y1 >= challenge.getCols() || y1 < 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("invalid coordinates [ " + x1 + " " + y1 + " ]").build());
        }

        boolean result = challenge.place(squareId1, x1, y1);

        if(!result) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("not able to place squareId: " + squareId1 + " at [ " + x1 + " " + y1 + " ] ; is square already used?").build());
        }

        return Response.ok().build();
    }

    @POST
    @Timed
    @Path("{challengeId}/action/clear")
    public Response clear(@PathParam("challengeId") Integer challengeId) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        challenge.clear();

        return Response.ok().build();
    }

    @GET
    @Timed
    @Path("{challengeId}/evaluate")
    @Produces(MediaType.APPLICATION_JSON)
    public PuzzleEvaluateReport evaluate(@PathParam("challengeId") Integer challengeId) {
        PuzzleState challenge = datastore.getElement(challengeId);

        if(challenge == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("challengeId: " + challengeId + " does not exist").build());
        }

        return PuzzleEvaluator.evaluate(challenge);
    }
}
