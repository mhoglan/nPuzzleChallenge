package project.health;

import com.codahale.metrics.health.HealthCheck;
import project.PuzzleBank;

public class PuzzleHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        if(PuzzleBank.getPuzzles().isEmpty()) {
            return Result.unhealthy("no puzzles exist!");
        }

        return Result.healthy();
    }
}