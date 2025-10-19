// java
package ecs;

import ecs.components.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Entity {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    private final int id;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity() { this.id = NEXT_ID.getAndIncrement(); }
    public int getId() { return id; }

    // Generic add method accepting any Component implementation
    public <T extends Component> void add(T comp) {
        if (comp == null) return;
        components.put(comp.getClass(), comp);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T get(Class<T> cls) {
        return (T) components.get(cls);
    }

    public <T extends Component> boolean has(Class<T> cls) {
        return components.containsKey(cls);
    }

    public <T extends Component> void remove(Class<T> cls) {
        components.remove(cls);
    }
}
