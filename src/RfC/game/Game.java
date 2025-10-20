package RfC.game;
import RfC.components.ResourceType;
import RfC.core.World;
import RfC.components.PlayerComponent;
import RfC.components.RegionComponent;
import RfC.core.Entity;
import RfC.systems.*;

public class Game {
    public static void main(String[] args) throws Exception {
        World world = new World();

        // Create two players as entities that also hold a RegionComponent for demo mapping
        Entity p1 = new Entity();
        PlayerComponent pc1 = new PlayerComponent("Alice");
        pc1.victoryPoints = 0;
        pc1.hasTradeAdvantage = true; // demo
        p1.addComponent(pc1);
        // Give Alice a region that produces WOOD on roll 4
        p1.addComponent(new RegionComponent(4, ResourceType.WOOD));
        world.addEntity(p1);

        Entity p2 = new Entity();
        PlayerComponent pc2 = new PlayerComponent("Bob");
        pc2.victoryPoints = 0;
        p2.addComponent(pc2);
        // Bob has region producing WOOD on 5
        p2.addComponent(new RegionComponent(5, ResourceType.WOOD));
        world.addEntity(p2);

        // Add systems in order: Dice -> Event -> Production -> Victory
        DiceSystem diceSystem = new DiceSystem();
        EventSystem eventSystem = new EventSystem();
        ProductionSystem productionSystem = new ProductionSystem();
        VictorySystem victorySystem = new VictorySystem();
        ActionSystem actionSystem = new ActionSystem();

        world.addSystem(diceSystem);
        world.addSystem(eventSystem);
        world.addSystem(productionSystem);
        world.addSystem(actionSystem);
        world.addSystem(victorySystem);

        // Run a few rounds to demonstrate behavior
        for (int turn=0; turn<6; turn++) {
            System.out.println("n=== World Update (turn " + (turn+1) + ") ===");
            world.update();
        }

        System.out.println("nFinal resources:");
        for (Entity e : world.getEntities()) {
            if (e.hasComponent(PlayerComponent.class)) {
                PlayerComponent p = e.getComponent(PlayerComponent.class);
                System.out.println(p.name + ": " + p.resources);
            }
        }
    }
}
