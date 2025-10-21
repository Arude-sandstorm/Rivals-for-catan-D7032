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
        Entity diceEntity = world.getEntities().stream()
                .filter(e -> e.hasComponent(DiceComponent.class)).findFirst().orElse(null);
        if (diceEntity == null) return;
        int roll = diceEntity.getComponent(DiceComponent.class).productionRoll;

        for (Entity e : world.getEntities()) {
            if (!e.hasComponent(PlayerComponent.class) || !e.hasComponent(RegionComponent.class)) continue;
            RegionComponent rc = e.getComponent(RegionComponent.class);
            if (rc.diceNumber == roll) {
                PlayerComponent pc = e.getComponent(PlayerComponent.class);
                // enforce cap: only add if region < cap
                if (rc.stored < rc.cap) {
                    rc.stored++;
                    pc.addResource(rc.produces, 1);
                    System.out.println(pc.name + " gains 1 " + rc.produces + " (region d"+rc.diceNumber+
                            " " + rc.stored + "/" + rc.cap + ")");
                } else {
                    System.out.println("Region d"+rc.diceNumber+" already at cap ("+rc.cap+"), no gain.");
                }
            }
        }
    }
}
