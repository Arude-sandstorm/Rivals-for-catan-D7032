// PlayerComponent.java
package RfC.components;
import RfC.core.Component;

import java.util.HashMap;
import java.util.Map;

public class PlayerComponent implements Component {
    public String name;
    public int victoryPoints = 0;
    public Map<ResourceType, Integer> resources = new HashMap<>();
    public int skillPoints = 0; // used by some events
    public boolean hasTradeAdvantage = false;

    public PlayerComponent(String name) {
        this.name = name;
        for (ResourceType r : ResourceType.values()) resources.put(r, 0);
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
}
