package RfC.systems;

import RfC.components.PlayerComponent;
import RfC.core.Entity;

import java.util.List;

public class VictorySystem extends GameSystem {
    @Override
    public void update(List<Entity> entities) {
        for (Entity e : entities) {
            if (e.hasComponent(PlayerComponent.class)) {
                PlayerComponent pc = e.getComponent(PlayerComponent.class);
                if (pc.victoryPoints >= 7) {
                    System.out.println("*** " + pc.name + " has won with " + pc.victoryPoints + " victory points! ***");
                }
            }
        }
    }
}
