// PlayerComponent.java
package RfC.components;
import RfC.core.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

public class PlayerComponent implements Component {
    public String name;

    // --- Victory and sub-point categories ---
    public int victoryPoints = 0;
    public int commercePoints = 0;  // Trade-related
    public int progressPoints = 0;  // Science/development
    public int strengthPoints = 0;  // Military/knighthood
    public int skillPoints = 0;     // Used for event comparisons

    // --- Resources ---
    public Map<ResourceType, Integer> resources = new HashMap<>();

    // --- Player-specific traits ---
    public boolean hasTradeAdvantage = false;

    public PlayerComponent(String name) {
        this.name = name;
        for (ResourceType r : ResourceType.values()) {
            resources.put(r, 0);
        }
    }

    public void addResource(ResourceType type, int amount) {
        resources.put(type, resources.getOrDefault(type, 0) + amount);
    }

    public int getResource(ResourceType type) {
        return resources.getOrDefault(type, 0);
    }

    public void setResource(ResourceType type, int amount) {
        resources.put(type, amount);
    }

    public void addVictoryPoints(int amount) {
        victoryPoints += amount;
    }

    public int totalPoints() {
        return victoryPoints + commercePoints + progressPoints + strengthPoints + skillPoints;
    }
}
