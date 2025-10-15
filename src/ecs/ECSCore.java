package ecs;

import java.util.*;

// --- ECS Core ---

public class ECSCore {

    public static class Entity {
        public final int id;
        public Entity(int id) { this.id = id; }
        @Override public String toString() { return "Entity#" + id; }
    }

    public interface Component { }

    public abstract static class SystemBase {
        public abstract void update(World world);
    }

    public static class World {
        private int nextEntityId = 0;
        private final Map<Class<? extends Component>, Map<Integer, Component>> components = new HashMap<>();
        private final List<SystemBase> systems = new ArrayList<>();

        public Entity createEntity() { return new Entity(nextEntityId++); }

        public <T extends Component> void addComponent(Entity e, T c) {
            components.computeIfAbsent(c.getClass(), k -> new HashMap<>()).put(e.id, c);
        }

        public <T extends Component> T getComponent(Entity e, Class<T> type) {
            return type.cast(components.getOrDefault(type, Map.of()).get(e.id));
        }

        @SafeVarargs
        public final List<Entity> getEntitiesWith(Class<? extends Component>... compTypes) {
            Set<Integer> ids = null;
            for (var t : compTypes) {
                var map = components.get(t);
                if (map == null) return List.of();
                if (ids == null) ids = new HashSet<>(map.keySet());
                else ids.retainAll(map.keySet());
            }
            if (ids == null) return List.of();
            return ids.stream().map(Entity::new).toList();
        }

        public void addSystem(SystemBase system) { systems.add(system); }
        public void update() { for (var s : systems) s.update(this); }
    }
}
