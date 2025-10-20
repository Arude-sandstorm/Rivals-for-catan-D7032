package RfC.systems;
import RfC.components.ResourceType;
import RfC.components.DiceComponent;
import RfC.components.PlayerComponent;
import RfC.core.Entity;

import java.util.List;
import java.util.Optional;

public class EventSystem extends GameSystem {

    @Override
    public void update(List<Entity> entities) {
        Optional<Entity> diceEntity = world.getEntities().stream().filter(e -> e.hasComponent(DiceComponent.class)).findFirst();
        if (!diceEntity.isPresent()) return;
        DiceComponent d = diceEntity.get().getComponent(DiceComponent.class);
        int eventRoll = d.eventRoll;
        // If brigand (1) resolve immediately before production.
        // Implement simplified behavior for key events: brigand, trade, celebration, plentiful (mapping PDF)
        System.out.println("Resolving event roll: " + eventRoll);
        Entity active = getActivePlayer();
        if (active == null) return;
        PlayerComponent pc = active.getComponent(PlayerComponent.class);

        switch (eventRoll) {
            case 1:
                // Brigand: if you have more than 7 resources you lose all WOOL and GOLD
                int totalResources = pc.resources.values().stream().mapToInt(Integer::intValue).sum();
                if (totalResources > 7) {
                    pc.setResource(ResourceType.WOOL, 0);
                    pc.setResource(ResourceType.GOLD, 0);
                    System.out.println(pc.name + " attacked by brigand: wool and gold removed.");
                } else {
                    System.out.println(pc.name + " attacked by brigand but has <=7 resources.");
                }
                break;
            case 2:
                // Trade: if player has trade advantage receive 1 resource of choice from opponent
                if (pc.hasTradeAdvantage) {
                    Entity opponent = getOpponent(active);
                    if (opponent != null) {
                        PlayerComponent op = opponent.getComponent(PlayerComponent.class);
                        // simple: take first non-zero resource from opponent, otherwise NONE
                        for (ResourceType rt : ResourceType.values()) {
                            if (rt==ResourceType.NONE) continue;
                            if (op.getResource(rt) > 0) {
                                op.addResource(rt, -1);
                                pc.addResource(rt, 1);
                                System.out.println(pc.name + " traded and took 1 " + rt + " from " + op.name);
                                break;
                            }
                        }
                    }
                }
                break;
            case 3:
                // Celebration: if you have most skill points you alone receive 1 resource of your choice.
                // Simplified: give active 1 WOOL.
                pc.addResource(ResourceType.WOOL, 1);
                System.out.println(pc.name + " receives 1 WOOL from Celebration.");
                break;
            case 4:
                // Plentiful Harvest: each player receives 1 resource of choice (simplified as GRAIN)
                for (Entity e : world.getEntities()) {
                    if (e.hasComponent(PlayerComponent.class)) {
                        e.getComponent(PlayerComponent.class).addResource(ResourceType.GRAIN, 1);
                    }
                }
                System.out.println("Plentiful Harvest: each player gets 1 GRAIN.");
                break;
            case 5:
            case 6:
                // Event card: not implemented fully here - simulate draw
                System.out.println("Event card: draw and resolve (not fully implemented).");
                break;
            default:
                break;
        }
    }

    private Entity getActivePlayer() {
        // naive: first entity with PlayerComponent is active for demo purposes
        return world.getEntities().stream().filter(e -> e.hasComponent(PlayerComponent.class)).findFirst().orElse(null);
    }

    private Entity getOpponent(Entity active) {
        return world.getEntities().stream().filter(e -> e.hasComponent(PlayerComponent.class) && !e.getId().equals(active.getId())).findFirst().orElse(null);
    }
}
