// PlayerComponent.java
package ecs.components;

import java.util.*;

public class PlayerComponent implements Component {
    public final Map<String,Integer> resources = new HashMap<>();
    public final List<Integer> hand = new ArrayList<>();
    public int victoryPoints = 0;
    public int gold = 0;
    public int stars = 0;
    public int strengthPoints = 0;
    public int commercePoints = 0;

    public PlayerComponent() {
        // init resources to zero for common types (example: G=grain, L=lumber, O=ore, W=wool, A=gold)
        resources.put("G",0); resources.put("L",0); resources.put("O",0); resources.put("W",0); resources.put("A",0);
    }
}
