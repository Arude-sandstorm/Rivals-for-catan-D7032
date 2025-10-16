package ecs;

import ecs.ECSCore.*;
import ecs.Components.*;
import ecs.Systems.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class RivalsCLI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println("=== Rivals for Catan (ECS CLI Edition) ===");
        System.out.print("Host (h), Join (j), or Play Offline vs Bot (b)? ");
        String mode = scanner.nextLine().trim().toLowerCase();

        if (mode.equals("h")) {
            new GameServer().runServer();
        } else if (mode.equals("j")) {
            new GameClient().runClient();
        } else {
            playOffline();
        }
    }

    // --- Offline mode (Player vs Bot) ---
    private static void playOffline() {
        ECSCore.World world = new ECSCore.World();

        // Create players
        Entity human = world.createEntity();
        world.addComponent(human, new PlayerComponent("You"));
        world.addComponent(human, new ResourceInventoryComponent());
        world.addComponent(human, new VictoryPointsComponent());

        Entity bot = world.createEntity();
        world.addComponent(bot, new PlayerComponent("Bot"));
        world.addComponent(bot, new ResourceInventoryComponent());
        world.addComponent(bot, new VictoryPointsComponent());

        // Regions
        Entity r1 = world.createEntity();
        world.addComponent(r1, new RegionComponent(ResourceType.WOOD, 6));
        world.addComponent(r1, new OwnerComponent(human));

        Entity r2 = world.createEntity();
        world.addComponent(r2, new RegionComponent(ResourceType.BRICK, 8));
        world.addComponent(r2, new OwnerComponent(bot));

        // Cards
        Map<ResourceType, Integer> roadCost = Map.of(ResourceType.WOOD, 1, ResourceType.BRICK, 1);
        Entity road1 = world.createEntity();
        world.addComponent(road1, new CardComponent("Road", roadCost, 1));
        world.addComponent(road1, new OwnerComponent(human));

        Entity road2 = world.createEntity();
        world.addComponent(road2, new CardComponent("Road", roadCost, 1));
        world.addComponent(road2, new OwnerComponent(bot));

        System.out.println("\nStarting game! First to 3 Victory Points wins.");

        var diceSystem = new DiceSystem();
        var productionSystem = new ProductionSystem();
        var victorySystem = new VictorySystem(3);

        Random rng = new Random();

        // --- Main loop ---
        for (int turn = 1; turn <= 50; turn++) {
            System.out.println("\n=== Turn " + turn + " ===");

            // Player turn
            System.out.println("\nYour turn:");
            diceSystem.update(world);
            productionSystem.update(world);
            showPlayerState(world, human);

            System.out.print("Play card if possible? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                var inv = world.getComponent(human, ResourceInventoryComponent.class);
                var vp = world.getComponent(human, VictoryPointsComponent.class);
                var card = world.getComponent(road1, CardComponent.class);
                if (inv.spend(card.cost)) {
                    vp.points += card.vp;
                    System.out.println("✅ You played " + card.name + " (+1 VP)");
                } else {
                    System.out.println("❌ Not enough resources.");
                }
            }

            victorySystem.update(world);
            if (checkVictory(world)) break;

            // --- Bot turn ---
            System.out.println("\nBot's turn...");
            diceSystem.update(world);
            productionSystem.update(world);
            botTurn(world, bot, road2);
            victorySystem.update(world);
            if (checkVictory(world)) break;

            showPlayerState(world, human);
            showPlayerState(world, bot);
        }

        System.out.println("\nGame over.");
    }

    private static void botTurn(World world, Entity bot, Entity card) {
        var inv = world.getComponent(bot, ResourceInventoryComponent.class);
        var vp = world.getComponent(bot, VictoryPointsComponent.class);
        var cardData = world.getComponent(card, CardComponent.class);
        if (inv.spend(cardData.cost)) {
            vp.points += cardData.vp;
            System.out.println("🤖 Bot plays " + cardData.name + " (+1 VP)");
        } else {
            System.out.println("🤖 Bot cannot afford to play a card this turn.");
        }
    }

    private static void showPlayerState(World w, Entity e) {
        var pc = w.getComponent(e, PlayerComponent.class);
        var inv = w.getComponent(e, ResourceInventoryComponent.class);
        var vp = w.getComponent(e, VictoryPointsComponent.class);
        System.out.println(pc.name + " -> " + inv + " | VP: " + vp.points);
    }

    private static boolean checkVictory(World w) {
        for (Entity e : w.getEntitiesWith(PlayerComponent.class, VictoryPointsComponent.class)) {
            var pc = w.getComponent(e, PlayerComponent.class);
            var vp = w.getComponent(e, VictoryPointsComponent.class);
            if (vp.points >= 3) {
                System.out.println("🏆 " + pc.name + " wins!");
                return true;
            }
        }
        return false;
    }
}
