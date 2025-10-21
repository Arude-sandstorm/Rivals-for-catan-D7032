package RfC.systems;
import RfC.components.CardComponent;
import RfC.components.DeckComponent;
import RfC.core.Entity;

import java.util.List;

public class EventDeckSystem extends GameSystem {
    private DeckComponent eventDeck;

    public EventDeckSystem(DeckComponent deck) {
        this.eventDeck = deck;
    }

    @Override
    public void update(List<Entity> entities) {
        // Passive system – triggered externally by EventSystem when dice = 5/6
    }

    public Entity drawEventCard() {
        if (eventDeck.size() == 0) {
            System.out.println("Event deck empty – reshuffling not implemented.");
            return null;
        }
        Entity card = eventDeck.draw();
        System.out.println("Drew event card: " + card.getComponent(CardComponent.class).name);
        return card;
    }
}

