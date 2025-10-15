package ecs;

import java.util.*;

import ecs.ECSCore.Component;
import ecs.ECSCore.Entity;

public class Components {

    public enum ResourceType { WOOD, BRICK, SHEEP, GRAIN, ORE }

    // --- Components ---

    public static class PlayerComponent implements Component {
        public final String name;
        public PlayerComponent(String name) { this.name = name; }
    }

    public static class ResourceInventoryComponent implements Component {
        public final EnumMap<ResourceType, Integer> resources = new EnumMap<>(ResourceType.class);
        public ResourceInventoryComponent() {
            for (var t : ResourceType.values()) resources.put(t, 0);
        }
        public void add(ResourceType t, int amount) {
            resources.put(t, resources.get(t) + amount);
        }
        public boolean spend(Map<ResourceType, Integer> cost) {
            for (var e : cost.entrySet())
                if (resources.get(e.getKey()) < e.getValue()) return false;
            cost.forEach((t, a) -> resources.put(t, resources.get(t) - a));
            return true;
        }
        public String toString() {
            return resources.toString();
        }
    }

    public static class RegionComponent implements Component {
        public ResourceType produces;
        public RegionComponent(ResourceType produces) { this.produces = produces; }
    }

    public static class OwnerComponent implements Component {
        public Entity owner;
        public OwnerComponent(Entity owner) { this.owner = owner; }
    }

    public static class CardComponent implements Component {
        public String name;
        public Map<ResourceType, Integer> cost;
        public int vp;
        public CardComponent(String name, Map<ResourceType, Integer> cost, int vp) {
            this.name = name; this.cost = cost; this.vp = vp;
        }
    }

    public static class VictoryPointsComponent implements Component {
        public int points = 0;
    }
}
