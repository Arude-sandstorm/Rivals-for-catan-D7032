package RfC.tests;
import static org.junit.jupiter.api.Assertions.*;

import RfC.components.PlayerComponent;
import RfC.core.Entity;
import RfC.core.World;
import RfC.systems.VictorySystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class VictorySystemTests {

    private World world;
    private Entity player1;
    private PlayerComponent pc1;
    private VictorySystem victorySystem;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup() {
        world = new World();
        victorySystem = new VictorySystem();

        // Redirect System.out so we can inspect printed output
        System.setOut(new PrintStream(outputStream));

        // Create test player
        player1 = new Entity();
        pc1 = new PlayerComponent("Alice");
        player1.addComponent(pc1);
        world.addEntity(player1);

        world.addSystem(victorySystem);
    }

    @Test
    void testVictoryPointsAloneCanWin() {
        pc1.victoryPoints = 7;
        world.update();

        String output = outputStream.toString();
        assertTrue(output.contains("Alice wins"), "Should announce Alice as the winner");
        assertTrue(output.contains("Victory:"), "Should include breakdown table");
    }

    @Test
    void testCombinedPointsReachVictoryThreshold() {
        pc1.victoryPoints = 3;
        pc1.commercePoints = 2;
        pc1.progressPoints = 1;
        pc1.skillPoints = 1; // total = 7

        world.update();

        String output = outputStream.toString();
        assertTrue(output.contains("Alice wins"), "Should declare winner when total >= 7");
    }

    @Test
    void testBelowThresholdNoWin() {
        pc1.victoryPoints = 2;
        pc1.commercePoints = 2;
        pc1.progressPoints = 1; // total = 5 < 7
        world.update();

        String output = outputStream.toString();
        assertFalse(output.contains("wins"), "Should not print win message for total < 7");
    }

    @Test
    void testAllCategoriesCounted() {
        pc1.victoryPoints = 1;
        pc1.commercePoints = 1;
        pc1.progressPoints = 1;
        pc1.strengthPoints = 1;
        pc1.skillPoints = 3; // total = 7

        world.update();

        String output = outputStream.toString();
        assertTrue(output.contains("TOTAL:    7"), "Breakdown should include correct total points");
    }

    @Test
    void testMultiplePlayersOnlyWinnerAnnounced() {
        // Second player
        Entity player2 = new Entity();
        PlayerComponent pc2 = new PlayerComponent("Bob");
        world.addEntity(player2);
        player2.addComponent(pc2);

        pc1.victoryPoints = 2;
        pc1.commercePoints = 2;
        pc1.progressPoints = 3; // 7 total
        pc2.victoryPoints = 1;
        pc2.progressPoints = 1; // 2 total

        world.update();

        String output = outputStream.toString();
        assertTrue(output.contains("Alice wins"), "Only Alice should win");
        assertFalse(output.contains("Bob wins"), "Bob should not be declared winner");
    }

//    @AfterEach
//    void restoreOutput() {
//        System.setOut(originalOut);
//    }
}
