package RfC.systems;
import RfC.components.ResourceType;
import RfC.components.DiceComponent;
import RfC.components.PlayerComponent;
import RfC.core.Entity;

import java.util.List;
import java.util.Optional;
import java.util.List;
import java.util.Optional;

public class EventSystem extends GameSystem {
    private EventDeckSystem eventDeckSystem;
    private EventCardEffectSystem eventEffectSystem;

    public EventSystem(EventDeckSystem deckSystem, EventCardEffectSystem effectSystem) {
        this.eventDeckSystem = deckSystem;
        this.eventEffectSystem = effectSystem;
    }

    @Override
    public void update(List<Entity> entities) {
        Optional<Entity> diceEntity = world.getEntities().stream()
                .filter(e -> e.hasComponent(DiceComponent.class))
                .findFirst();
        if (!diceEntity.isPresent()) return;

        DiceComponent d = diceEntity.get().getComponent(DiceComponent.class);
        int eventRoll = d.eventRoll;

        Entity active = getActivePlayer();
        if (active == null) return;

        switch (eventRoll) {
            case 1:
                handleBrigand(active);
                break;
            case 2:
                handleTrade(active);
                break;
            case 3:
                handleCelebration(active);
                break;
            case 4:
                handleHarvest();
                break;
            case 5:
            case 6:
                // Event card draw
                Entity eventCard = eventDeckSystem.drawEventCard();
                if (eventCard != null) {
                    eventEffectSystem.resolveEventCard(eventCard, active);
                }
                break;
        }
    }

    private Entity getActivePlayer() {
        return world.getEntities().stream()
                .filter(e -> e.hasComponent(PlayerComponent.class))
                .findFirst().orElse(null);
    }

    private Entity getOpponent(Entity active) {
        return world.getEntities().stream()
                .filter(e -> e.hasComponent(PlayerComponent.class)
                        && !e.getId().equals(active.getId()))
                .findFirst().orElse(null);
    }

    private void handleBrigand(Entity active) {
        PlayerComponent pc = active.getComponent(PlayerComponent.class);
        int total = pc.resources.values().stream().mapToInt(Integer::intValue).sum();
        if (total > 7) {
            pc.setResource(ResourceType.WOOL, 0);
            pc.setResource(ResourceType.GOLD, 0);
            System.out.println(pc.name + " loses wool and gold (Brigand event).");
        }
    }

    private void handleTrade(Entity active) {
        PlayerComponent pc = active.getComponent(PlayerComponent.class);
        // grant or use trade advantage — simple approach:
        pc.hasTradeAdvantage = true; // e.g., event grants it
        Entity opponent = getOpponent(active);
        if (opponent == null) return;
        if (!pc.hasTradeAdvantage) return;

        PlayerComponent op = opponent.getComponent(PlayerComponent.class);
        for (ResourceType rt : ResourceType.values()) {
            if (rt != ResourceType.NONE && op.getResource(rt) > 0) {
                op.addResource(rt, -1);
                pc.addResource(rt, 1);
                System.out.println(pc.name + " takes 1 " + rt + " from " + op.name);
                break;
            }
        }
    }

    private void handleCelebration(Entity active) {
        PlayerComponent pc = active.getComponent(PlayerComponent.class);
        // Award resource and skill advantage marker
        pc.addResource(ResourceType.WOOL, 1);
        pc.hasSkillAdvantage = true;
        System.out.println(pc.name + " receives 1 WOOL and gains Skill advantage.");
    }


    private void handleHarvest() {
        for (Entity e : world.getEntities()) {
            if (e.hasComponent(PlayerComponent.class)) {
                e.getComponent(PlayerComponent.class).addResource(ResourceType.GRAIN, 1);
            }
        }
        System.out.println("All players receive 1 GRAIN (Plentiful Harvest).");
    }
}
