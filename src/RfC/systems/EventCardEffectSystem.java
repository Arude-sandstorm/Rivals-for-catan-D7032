package RfC.systems;
import RfC.components.CardComponent;
import RfC.components.PlayerComponent;
import RfC.components.ResourceType;
import RfC.core.Entity;

import java.util.List;

public class EventCardEffectSystem extends GameSystem {

    public void resolveEventCard(Entity card, Entity activePlayer) {
        String name = card.getComponent(CardComponent.class).cardName.toLowerCase();
        PlayerComponent pc = activePlayer.getComponent(PlayerComponent.class);

        switch (name) {
            case "brigand attack":
                pc.setResource(ResourceType.WOOL, 0);
                pc.setResource(ResourceType.GOLD, 0);
                System.out.println(pc.name + " hit by Brigand Attack (event card).");
                break;

            case "trade advantage":
                pc.hasTradeAdvantage = true;
                System.out.println(pc.name + " gains trade advantage.");
                break;

            case "celebration":
                pc.addResource(ResourceType.WOOL, 1);
                System.out.println(pc.name + " gains 1 WOOL for celebration.");
                break;

            case "plentiful harvest":
                for (Entity e : world.getEntities()) {
                    if (e.hasComponent(PlayerComponent.class)) {
                        e.getComponent(PlayerComponent.class).addResource(ResourceType.GRAIN, 1);
                    }
                }
                System.out.println("All players gain 1 GRAIN for plentiful harvest.");
                break;

            // Add more cases as you model the 9 event cards
            default:
                System.out.println("Unhandled event card: " + name);
                break;
        }
    }

    @Override
    public void update(List<Entity> entities) {
        // Passive – called by EventSystem when an event card is drawn
    }
}
