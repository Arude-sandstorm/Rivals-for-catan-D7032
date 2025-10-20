package RfC.tests;
import static org.junit.jupiter.api.Assertions.*;

import RfC.components.DeckComponent;
import RfC.components.DiceComponent;
import RfC.components.PlayerComponent;
import RfC.components.ResourceType;
import RfC.core.Entity;
import RfC.core.World;
import RfC.systems.EventCardEffectSystem;
import RfC.systems.EventDeckSystem;
import RfC.systems.EventSystem;
import RfC.systems.VictorySystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

public class GameCoreTests {

    private World world;
    private Entity player1, player2;
    private PlayerComponent pc1, pc2;
    private DiceComponent dice;

    @BeforeEach
    void setup() {
        world = new World();

        player1 = new Entity();
        pc1 = new PlayerComponent("Alice");
        player1.addComponent(pc1);
        world.addEntity(player1);

        player2 = new Entity();
        pc2 = new PlayerComponent("Bob");
        world.addEntity(player2);
        player2.addComponent(pc2);

        Entity diceEntity = new Entity();
        dice = new DiceComponent();
        diceEntity.addComponent(dice);
        world.addEntity(diceEntity);
    }

    @Test
    void testTwoPlayersExist() {
        long count = world.getEntities().stream()
                .filter(e -> e.hasComponent(PlayerComponent.class))
                .count();
        assertEquals(2, count, "There should be exactly two player entities");
    }

    @Test
    void testBrigandEventRemovesWoolAndGold() {
        EventDeckSystem eventDeckSystem = new EventDeckSystem(new DeckComponent("Events"));
        EventCardEffectSystem effectSystem = new EventCardEffectSystem();
        EventSystem eventSystem = new EventSystem(eventDeckSystem, effectSystem);

        // Give Alice resources > 7 and set dice event = 1 (Brigand)
        pc1.addResource(ResourceType.WOOL, 4);
        pc1.addResource(ResourceType.GOLD, 4);
        dice.eventRoll = 1;

        world.addSystem(eventSystem);
        world.update();

        assertEquals(0, pc1.getResource(ResourceType.WOOL));
        assertEquals(0, pc1.getResource(ResourceType.GOLD));
    }

    @Test
    void testTradeEventTransfersResource() {
        pc1.hasTradeAdvantage = true;
        pc2.addResource(ResourceType.WOOD, 2);

        EventDeckSystem eventDeckSystem = new EventDeckSystem(new DeckComponent("Events"));
        EventCardEffectSystem effectSystem = new EventCardEffectSystem();
        EventSystem eventSystem = new EventSystem(eventDeckSystem, effectSystem);

        dice.eventRoll = 2; // trade event
        world.addSystem(eventSystem);
        world.update();

        assertTrue(pc1.getResource(ResourceType.WOOD) >= 1,
                "Alice should gain at least one WOOD from trade");
    }

    @Test
    void testCelebrationAddsResource() {
        EventDeckSystem eventDeckSystem = new EventDeckSystem(new DeckComponent("Events"));
        EventCardEffectSystem effectSystem = new EventCardEffectSystem();
        EventSystem eventSystem = new EventSystem(eventDeckSystem, effectSystem);

        dice.eventRoll = 3;
        world.addSystem(eventSystem);
        world.update();

        assertEquals(1, pc1.getResource(ResourceType.WOOL),
                "Celebration should give the active player 1 WOOL");
    }

    @Test
    void testPlentifulHarvestGivesAllPlayersGrain() {
        EventDeckSystem eventDeckSystem = new EventDeckSystem(new DeckComponent("Events"));
        EventCardEffectSystem effectSystem = new EventCardEffectSystem();
        EventSystem eventSystem = new EventSystem(eventDeckSystem, effectSystem);

        dice.eventRoll = 4; // Plentiful harvest
        world.addSystem(eventSystem);
        world.update();

        assertEquals(1, pc1.getResource(ResourceType.GRAIN));
        assertEquals(1, pc2.getResource(ResourceType.GRAIN));
    }

    @Test
    void testVictoryConditionTriggers() {
        VictorySystem victorySystem = new VictorySystem();
        pc1.victoryPoints = 7;
        world.addSystem(victorySystem);

        // Should print win message and pass
        assertDoesNotThrow(() -> world.update(),
                "VictorySystem should safely detect a win");
    }
}
