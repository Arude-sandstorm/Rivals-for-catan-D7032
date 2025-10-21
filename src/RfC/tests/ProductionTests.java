package RfC.tests;
import static org.junit.jupiter.api.Assertions.*;

import RfC.components.DiceComponent;
import RfC.components.PlayerComponent;
import RfC.components.RegionComponent;
import RfC.components.ResourceType;
import RfC.core.Entity;
import RfC.core.World;
import RfC.systems.ProductionSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductionTests {

    private World world;
    private ProductionSystem productionSystem;
    private Entity player1;
    private PlayerComponent pc1;
    private DiceComponent dice;

    @BeforeEach
    void setup() {
        world = new World();

        // Create player and give her a region producing WOOD on dice = 4
        player1 = new Entity();
        pc1 = new PlayerComponent("Alice");
        player1.addComponent(pc1);
        player1.addComponent(new RegionComponent(4, ResourceType.WOOD));
        world.addEntity(player1);

        // Create dice entity
        Entity diceEntity = new Entity();
        dice = new DiceComponent();
        diceEntity.addComponent(dice);
        world.addEntity(diceEntity);

        productionSystem = new ProductionSystem();
        world.addSystem(productionSystem);
    }

    @Test
    void testPlayerGainsResourceOnMatchingDice() {
        dice.productionRoll = 4;
        world.update();

        int wood = pc1.getResource(ResourceType.WOOD);
        assertEquals(1, wood, "Player should gain 1 WOOD on roll 4");
    }

    @Test
    void testNoProductionIfNoMatchingRegion() {
        dice.productionRoll = 5;
        world.update();

        int wood = pc1.getResource(ResourceType.WOOD);
        assertEquals(0, wood, "Player should gain 0 WOOD on non-matching roll");
    }

    @Test
    void testMultiplePlayersGainIfRegionsMatch() {
        // Add a second player with same region number
        Entity player2 = new Entity();
        PlayerComponent pc2 = new PlayerComponent("Bob");
        player2.addComponent(pc2);
        player2.addComponent(new RegionComponent(4, ResourceType.WOOD));
        world.addEntity(player2);

        dice.productionRoll = 4;
        world.update();

        assertEquals(1, pc1.getResource(ResourceType.WOOD));
        assertEquals(1, pc2.getResource(ResourceType.WOOD));
    }
}
