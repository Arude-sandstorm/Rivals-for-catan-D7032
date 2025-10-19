// World.java
package ecs;

import ecs.components.Component;

import java.util.*;
import java.util.stream.Collectors;

public class World {
    private final Map<Integer, Entity> entities = new HashMap<>();
    private final List<System> systems = new ArrayList<>();

    public Entity createEntity() {
        Entity e = new Entity();
        entities.put(e.getId(), e);
        return e;
    }

    public Optional<Entity> getEntity(int id) {
        return Optional.ofNullable(entities.get(id));
    }

    public List<Entity> allEntities() {
        return new ArrayList<>(entities.values());
    }

    public void addSystem(System s) { systems.add(s); }
    public void update(double dt) {
        for (System s : systems) s.update(this, dt);
    }

    public List<Entity> entitiesWith(Class<? extends Component>... comps) {
        return entities.values().stream()
                .filter(e -> {
                    for (Class<? extends Component> c : comps) if (!e.has(c)) return false;
                    return true;
                })
                .collect(Collectors.toList());
    }
}







