package RfC.systems;

import RfC.components.DiceComponent;
import RfC.components.PlayerComponent;
import RfC.components.RegionComponent;
import RfC.core.Entity;

import java.util.List;
import java.util.stream.Collectors;

public class ProductionSystem extends GameSystem {
    @Override
    public void update(List<Entity> entities) {
        // Find dice
        Entity diceEntity = world.getEntities().stream().filter(e -> e.hasComponent(DiceComponent.class)).findFirst().orElse(null);
        if (diceEntity == null) return;
        DiceComponent d = diceEntity.getComponent(DiceComponent.class);
        int prod = d.productionRoll;
        System.out.println("Resolving production for roll=" + prod);
        // For demo: any player with a RegionComponent matching the number gets 1 WOOD.
        for (Entity e : world.getEntities()) {
            if (e.hasComponent(PlayerComponent.class) && e.hasComponent(RegionComponent.class)) {
                RegionComponent rc = e.getComponent(RegionComponent.class);
                if (rc.diceNumber == prod) {
                    e.getComponent(PlayerComponent.class).addResource(rc.produces, 1);
                    System.out.println(e.getComponent(PlayerComponent.class).name + " gains 1 " + rc.produces);
                }
            }
        }
    }



}
