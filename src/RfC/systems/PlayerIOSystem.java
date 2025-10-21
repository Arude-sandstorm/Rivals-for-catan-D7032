package RfC.systems;
import RfC.components.PlayerComponent;
import RfC.components.PlayerIOComponent;
import RfC.core.Entity;
import RfC.core.World;
import RfC.util.ConsoleRenderer;
import java.util.List;
import java.util.List;
import java.util.List;
import java.util.Scanner;

public class PlayerIOSystem extends GameSystem {
    private final ConsoleRenderer renderer = new ConsoleRenderer();

    @Override
    public void update(List<Entity> entities) {
        // Not auto-updating each tick
    }

    public void printGameState(World world, Entity activePlayer) {
        for (Entity e : world.getEntities()) {
            if (e.hasComponent(PlayerComponent.class)) {
                PlayerComponent pc = e.getComponent(PlayerComponent.class);
                System.out.println((e == activePlayer ? "👉 " : "") + pc.name + "'s Principality:");
                System.out.println(renderer.renderPrincipality(e, world));
                System.out.println(renderer.renderResources(pc));
                System.out.println(renderer.renderHand(e));
            }
        }
    }

    public void refreshDisplay(Entity player, World world) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printGameState(world, player);
    }

    // ✅ Needed by Game.java
    public String getPlayerChoice(Entity player, String prompt, String[] options) {
        Scanner input = new Scanner(System.in);
        System.out.println(prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, options[i]);
        }

        int choice = -1;
        while (choice < 1 || choice > options.length) {
            System.out.print("Select an option: ");
            try {
                choice = Integer.parseInt(input.nextLine().trim());
            } catch (Exception e) {
                choice = -1;
            }
        }
        return options[choice - 1];
    }

    // ✅ Needed by Game.java
    public void sendMessage(Entity player, String msg) {
        PlayerComponent pc = player.getComponent(PlayerComponent.class);
        System.out.println(pc.name + ": " + msg);
    }
}
