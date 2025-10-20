package RfC.core;
import RfC.systems.GameSystem;
import RfC.systems.EventSystem;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<Entity> entities = new ArrayList<>();
    private final List<GameSystem> systems = new ArrayList<>();

    public void addEntity(Entity e) { entities.add(e); }
    public List<Entity> getEntities() { return entities; }

    public void addSystem(GameSystem s) {
        s.setWorld(this);
        systems.add(s);
    }

    public void update() {
        for (GameSystem s : systems) {
            s.update(entities);
        }
    }
}
