package project;

/**
 * Created by michaelhoglan on 9/7/15.
 */
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import project.configuration.PuzzleConfiguration;
import project.health.PuzzleHealthCheck;
import project.resources.ChallengeResource;
import project.resources.PuzzleResource;

public class PuzzleApplication extends Application<PuzzleConfiguration> {
    public static void main(String[] args) throws Exception {
        new PuzzleApplication().run(args);
    }

    @Override
    public String getName() {
        return "nPuzzleChallenge";
    }

    @Override
    public void initialize(Bootstrap<PuzzleConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(PuzzleConfiguration configuration,
                    Environment environment) {
        final PuzzleResource puzzleResource = new PuzzleResource();
        final ChallengeResource challengeResource = new ChallengeResource();
        environment.jersey().register(puzzleResource);
        environment.jersey().register(challengeResource);
        environment.healthChecks().register("puzzleCheck", new PuzzleHealthCheck());
    }
}