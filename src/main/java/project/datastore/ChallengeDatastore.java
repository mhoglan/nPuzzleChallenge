package project.datastore;

import project.PuzzleState;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michaelhoglan on 9/8/15.
 */
public class ChallengeDatastore {

    private int lastId = 1;

    private Map<Integer, PuzzleState> challenges= new HashMap<>();

    public ChallengeDatastore() {

    }

    public PuzzleState getElement(Integer challengeId) {
        if(!challenges.containsKey(challengeId)) {
            return null;
        }

        return challenges.get(challengeId);
    }

    public Integer insertElement(PuzzleState challenge) {
        int elementId = lastId++;

        challenges.put(elementId, challenge);

        return elementId;
    }

    public PuzzleState removeElement(Integer challengeId) {
        if(!challenges.containsKey(challengeId)) {
            return null;
        }

        return challenges.remove(challengeId);
    }
}
