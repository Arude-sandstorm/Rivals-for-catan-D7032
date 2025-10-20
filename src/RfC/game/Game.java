package RfC.game;
import RfC.components.*;
import RfC.core.CardType;
import RfC.core.World;
import RfC.core.Entity;
import RfC.systems.*;
import RfC.util.CardFactory;

import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public static void main(String[] args) throws Exception {
        World world = new World();

        // Create players
        Entity p1 = new Entity();
        PlayerComponent pc1 = new PlayerComponent("Alice");
        p1.addComponent(pc1);
        world.addEntity(p1);

        Entity p2 = new Entity();
        PlayerComponent pc2 = new PlayerComponent("Bob");
        world.addEntity(p2);
        p2.addComponent(pc2);

        // Load all cards
        //List<Entity> allCards = CardFactory.loadCardsFromJson("cards.json");
        List<Entity> allCards = CardFactory.loadCardsFromJson("src/RfC/core/cards.json");

        // Create decks
        DeckComponent eventDeck = new DeckComponent("Event Deck");
        DeckComponent basicDeck1 = new DeckComponent("Draw Stack 1");
        DeckComponent basicDeck2 = new DeckComponent("Draw Stack 2");
        DeckComponent basicDeck3 = new DeckComponent("Draw Stack 3");
        DeckComponent basicDeck4 = new DeckComponent("Draw Stack 4");

        // Distribute cards based on type
        for (Entity c : allCards) {
            CardComponent card = c.getComponent(CardComponent.class);
            if (card.cardType == CardType.EVENT) eventDeck.add(c);
            else basicDeck1.add(c); // Simplified – you can distribute evenly among 4 stacks
        }

        eventDeck.shuffle();
        basicDeck1.shuffle();

        // Add systems
        DiceSystem diceSystem = new DiceSystem();
        EventDeckSystem eventDeckSystem = new EventDeckSystem(eventDeck);
        EventCardEffectSystem eventCardEffectSystem = new EventCardEffectSystem();
        EventSystem eventSystem = new EventSystem(eventDeckSystem, eventCardEffectSystem);
        ProductionSystem productionSystem = new ProductionSystem();
        VictorySystem victorySystem = new VictorySystem();

        world.addSystem(diceSystem);
        world.addSystem(eventSystem);
        world.addSystem(productionSystem);
        world.addSystem(victorySystem);

        // Run a few rounds
        for (int i = 0; i < 6; i++) {
            System.out.println("\n=== Round " + (i + 1) + " ===");
            world.update();
        }
    }
}
