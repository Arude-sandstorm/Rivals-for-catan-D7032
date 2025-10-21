package RfC.components;

import RfC.core.Component;
import RfC.core.Entity;

import java.util.HashMap;
import java.util.Map;


public class BoardComponent implements Component {
    // key: "r,c" → value: Entity
    public Map<String, Entity> grid = new HashMap<>();
    public int rows = 5;
    public int cols = 5;

    public void addEntityAt(Entity e, int row, int col) {
        grid.put(row + "," + col, e);
    }

    public Entity getEntityAt(int row, int col) {
        return grid.get(row + "," + col);
    }

    public void expandIfNeeded(int newCol) {
        if (newCol >= cols) cols = newCol + 1;
    }
}
