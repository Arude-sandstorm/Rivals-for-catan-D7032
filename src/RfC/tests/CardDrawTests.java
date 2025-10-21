package RfC.tests;
import static org.junit.jupiter.api.Assertions.*;

import RfC.components.CardComponent;
import RfC.components.DeckComponent;
import RfC.components.PlayerComponent;
import RfC.core.CardType;
import RfC.core.Entity;
import RfC.core.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

public class CardDrawTests {

    private World world;
    private Entity player1;
    private PlayerComponent pc1;
    private DeckComponent drawStack;
    private List<Entity> testCards;

    @BeforeEach
    void setup() {
        world = new World();

        // Create a simple player
        player1 = new Entity();
        pc1 = new PlayerComponent("Alice");
        player1.addComponent(pc1);
        world.addEntity(player1);

        // Create a test draw stack with dummy cards
        drawStack = new DeckComponent("Basic Stack");
        testCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Entity card = new Entity();
            card.addComponent(new CardComponent("Card" + i, CardType.BASIC));
            testCards.add(card);
        }
        drawStack.addAll(testCards);
        drawStack.shuffle();
    }

    @Test
    void testInitialDrawOfThreeCards() {
        // Simulate drawing 3 cards from the stack
        List<Entity> hand = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            hand.add(drawStack.draw());
        }

        assertEquals(3, hand.size(), "Player should draw exactly 3 cards");
        assertEquals(2, drawStack.size(), "Stack should have 2 cards remaining after draw");
    }

    @Test
    void testExchangeCardBetweenHandAndStack() {
        // Player draws 3 cards
        List<Entity> hand = new ArrayList<>();
        for (int i = 0; i < 3; i++) hand.add(drawStack.draw());
        assertEquals(2, drawStack.size());

        // Exchange 1 card back to the bottom and draw a new one
        Entity returned = hand.remove(0);
        drawStack.returnToBottom(returned);
        hand.add(drawStack.draw());

        assertEquals(3, hand.size(), "Player hand size remains constant after exchange");
        assertEquals(2, drawStack.size(), "Stack size remains constant after exchange");
    }

    @Test
    void testDrawFromEmptyStackReturnsNull() {
        DeckComponent empty = new DeckComponent("Empty");
        assertNull(empty.draw(), "Drawing from an empty deck should return null");
    }
}
