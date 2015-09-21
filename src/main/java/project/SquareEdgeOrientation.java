package project;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Hoglan
 */
public enum SquareEdgeOrientation {
    WILDCARD(-1),
    N(0),
    E(1),
    S(2),
    W(3);

    private int value;

    private static final Map<Integer, SquareEdgeOrientation> lookupMap = new HashMap<>();

    static {
        for(SquareEdgeOrientation o : SquareEdgeOrientation.values()) {
            lookupMap.put(o.toInt(), o);
        }
    }

    SquareEdgeOrientation(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static SquareEdgeOrientation fromInt(int value) {
        return lookupMap.get(value);
    }
}
