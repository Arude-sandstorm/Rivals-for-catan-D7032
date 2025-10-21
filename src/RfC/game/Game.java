package RfC.game;
import RfC.components.*;
import RfC.core.CardType;
import RfC.core.World;
import RfC.core.Entity;
import RfC.systems.*;
import RfC.util.CardFactory;
import java.util.*;
import RfC.systems.PlayerIOSystem;
import RfC.util.ConsoleRenderer;

public class Game {
    public static void main(String[] args) {
        World world = new World();
        PlayerIOSystem ioSystem = new PlayerIOSystem();
        ConsoleRenderer renderer = new ConsoleRenderer();

        // --- Setup players ---
        Entity p1 = createPlayer(world, "Alice");
        Entity p2 = createPlayer(world, "Bob");
        List<Entity> players = List.of(p1, p2);

        // --- Systems ---
        DiceSystem diceSystem = new DiceSystem();
        EventDeckSystem eventDeckSystem = new EventDeckSystem(new DeckComponent("Events"));
        EventCardEffectSystem eventEffectSystem = new EventCardEffectSystem();
        EventSystem eventSystem = new EventSystem(eventDeckSystem, eventEffectSystem);
        ProductionSystem productionSystem = new ProductionSystem();
        VictorySystem victorySystem = new VictorySystem();

        world.addSystem(diceSystem);
        world.addSystem(eventSystem);
        world.addSystem(productionSystem);
        world.addSystem(victorySystem);

        // --- Game state ---
        int current = new Random().nextBoolean() ? 0 : 1;
        boolean running = true;

        System.out.println("=== Rivals for Catan (CLI Edition) ===\n");

        while (running) {
            Entity active = players.get(current);
            Entity other = players.get((current + 1) % players.size());
            PlayerComponent activePC = active.getComponent(PlayerComponent.class);

            ioSystem.refreshDisplay(active, world);
            ioSystem.sendMessage(active, "Your turn begins!");

            // ---- PHASE 1: Roll Dice ----
            DiceComponent dice = new DiceComponent();
            Entity diceEntity = new Entity();
            diceEntity.addComponent(dice);
            world.addEntity(diceEntity);

            dice.productionRoll = new Random().nextInt(6) + 1;
            dice.eventRoll = new Random().nextInt(6) + 1;

            ioSystem.sendMessage(active, "EventDie → " + dice.eventRoll);
            ioSystem.sendMessage(active, "ProductionDie → " + dice.productionRoll);

            // Apply production first or event first depending on event type
            if (dice.eventRoll == 1) { // brigand first
                eventSystem.update(world.getEntities());
                productionSystem.update(world.getEntities());
            } else {
                productionSystem.update(world.getEntities());
                eventSystem.update(world.getEntities());
            }

            ioSystem.refreshDisplay(active, world);

            // ---- PHASE 2: Action Phase ----
            runActionPhase(active, world, ioSystem);
            ioSystem.refreshDisplay(active, world);

            // ---- PHASE 3: End of Turn & Victory ----
            victorySystem.update(world.getEntities());
            if (activePC.victoryPoints >= 7) {
                ioSystem.sendMessage(active, "🎉 You win the game with 7 VP!");
                ioSystem.refreshDisplay(active, world);
                running = false;
            } else {
                ioSystem.sendMessage(active, "End of your turn.");
                current = (current + 1) % players.size();
            }
        }

        System.out.println("\nGame Over. Thanks for playing!");
    }

    // --- Helper methods ---

    private static Entity createPlayer(World world, String name) {
        Entity player = new Entity();
        PlayerComponent pc = new PlayerComponent(name);
        BoardComponent board = new BoardComponent();
        PlayerIOComponent io = new PlayerIOComponent();

        player.addComponent(pc);
        player.addComponent(board);
        player.addComponent(io);
        world.addEntity(player);

        addStartingBoard(player, board);
        return player;
    }


    private static void addStartingBoard(Entity player, BoardComponent board) {
        // --- LEFT region ---
        Entity forest = new Entity();
        forest.addComponent(new CardComponent("Forest (L):Lumber", CardType.REGION));
        forest.addComponent(new RegionComponent(3, ResourceType.WOOD));
        forest.addComponent(new PositionComponent(2, 0));
        board.addEntityAt(forest, 2, 0);

        // --- LEFT settlement center ---
        Entity settlementLeft = new Entity();
        settlementLeft.addComponent(new CardComponent("Settlement", CardType.SETTLEMENT));
        settlementLeft.addComponent(new PositionComponent(2, 1));
        board.addEntityAt(settlementLeft, 2, 1);

        // --- Center road ---
        Entity roadCenter = new Entity();
        roadCenter.addComponent(new CardComponent("Road", CardType.ROAD));
        roadCenter.addComponent(new PositionComponent(2, 2));
        board.addEntityAt(roadCenter, 2, 2);

        // --- RIGHT settlement center ---
        Entity settlementRight = new Entity();
        settlementRight.addComponent(new CardComponent("Settlement", CardType.SETTLEMENT));
        settlementRight.addComponent(new PositionComponent(2, 3));
        board.addEntityAt(settlementRight, 2, 3);

        // --- RIGHT region ---
        Entity hill = new Entity();
        hill.addComponent(new CardComponent("Hill (B):Brick", CardType.REGION));
        hill.addComponent(new RegionComponent(4, ResourceType.BRICK));
        hill.addComponent(new PositionComponent(2, 4));
        board.addEntityAt(hill, 2, 4);

        // Set victory points for settlements
        PlayerComponent pc = player.getComponent(PlayerComponent.class);
        if (pc != null) pc.victoryPoints += 2; // 1 VP per starting settlement

        // Update board column limit
        board.cols = 5;
    }


    private static void runActionPhase(Entity player, World world, PlayerIOSystem io) {
        PlayerComponent pc = player.getComponent(PlayerComponent.class);
        boolean inActionPhase = true;
        while (inActionPhase) {
            String choice = io.getPlayerChoice(player, "Action Phase: Choose an option",
                    new String[]{"Build Road", "Trade Resource", "End Turn"});
            switch (choice) {
                case "Build Road":
                    buildRoad(player, world, io);
                    break;
                case "Trade Resource":
                    tradeResource(player, io);
                    break;
                case "End Turn":
                    inActionPhase = false;
                    break;
            }
        }
    }

    private static void buildRoad(Entity player, World world, PlayerIOSystem io) {
        BoardComponent board = player.getComponent(BoardComponent.class);
        PlayerComponent pc = player.getComponent(PlayerComponent.class);
        // Build road to the right of last settlement (simplified)
        int newCol = board.cols;
        Entity road = new Entity();
        road.addComponent(new CardComponent("Road", CardType.ROAD));
        road.addComponent(new PositionComponent(2, newCol));
        board.addEntityAt(road, 2, newCol);
        board.expandIfNeeded(newCol);
        pc.resources.put(ResourceType.BRICK, Math.max(0, pc.getResource(ResourceType.BRICK) - 1));
        pc.resources.put(ResourceType.WOOD, Math.max(0, pc.getResource(ResourceType.WOOD) - 1));
        io.sendMessage(player, "Built a road at column " + newCol + ".");
    }

    private static void tradeResource(Entity player, PlayerIOSystem io) {
        PlayerComponent pc = player.getComponent(PlayerComponent.class);
        String give = io.getPlayerChoice(player, "Choose resource to give", new String[]{"Brick", "Wood", "Wool", "Ore", "Grain"});
        String get = io.getPlayerChoice(player, "Choose resource to gain", new String[]{"Brick", "Wood", "Wool", "Ore", "Grain"});

        ResourceType giveType = ResourceType.valueOf(give.toUpperCase());
        ResourceType getType = ResourceType.valueOf(get.toUpperCase());

        if (pc.getResource(giveType) > 0) {
            pc.resources.put(giveType, pc.getResource(giveType) - 1);
            pc.resources.put(getType, pc.getResource(getType) + 1);
            io.sendMessage(player, "Traded 1 " + give + " for 1 " + get + ".");
        } else {
            io.sendMessage(player, "You don't have any " + give + " to trade.");
        }
    }
}
