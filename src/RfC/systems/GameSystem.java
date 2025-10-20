package RfC.systems;
import RfC.core.World;
import RfC.core.Entity;

import java.util.List;

public abstract class GameSystem {
    protected World world;

    public void setWorld(World w) { this.world = w; }

    // Called each "tick" or when the system should process
    public abstract void update(List<Entity> entities);
}
