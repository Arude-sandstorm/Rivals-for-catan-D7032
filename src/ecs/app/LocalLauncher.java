
package ecs.app;

import ecs.World;
import ecs.Entity;
import ecs.components.PlayerComponent;
import ecs.components.PrincipalityComponent;
import ecs.util.PrincipalityPrinter;
import ecs.systems.ActionQueue;
import ecs.systems.InputSystem;
import ecs.net.NetworkMessage;

import java.util.List;

/**
 * Minimal local CLI launcher for manual play/testing.
 * - creates a World, one local Player entity with PrincipalityComponent
 * - starts the InputSystem (reads stdin into ACTION messages)
 * - main loop drains ActionQueue and handles simple commands: 'board' and 'quit'
 *
 * Adjust package/class names if your project structure differs.
 */
public class LocalLauncher {
    public static void main(String[] args) throws Exception {
        World world = new World();

        // create a local player entity
        Entity player = world.createEntity();
        player.add(new PlayerComponent());
        player.add(new PrincipalityComponent());
        int playerId = player.getId();

        // action queue + CLI input system (InputSystem will wrap lines as ACTION messages)
        ActionQueue actionQueue = new ActionQueue();
        InputSystem input = new InputSystem(world, playerId, actionQueue);
        input.start();

        System.out.println("Local CLI launcher started. Commands: board | quit");
        boolean running = true;
        while (running) {
            // process all pending CLI actions
            List<NetworkMessage> msgs = actionQueue.drainAll();
            for (NetworkMessage m : msgs) {
                if (m == null || m.payload == null) continue;
                Object o = m.payload.get("cmd");
                if (!(o instanceof String)) continue;
                String cmd = ((String) o).trim();
                if ("board".equalsIgnoreCase(cmd)) {
                    PrincipalityComponent pc = player.get(PrincipalityComponent.class);
                    PlayerComponent pcComp = player.get(PlayerComponent.class);
                    String out = PrincipalityPrinter.printPrincipality(pc, pcComp, id -> null);
                    System.out.println(out);
                } else if ("quit".equalsIgnoreCase(cmd) || "exit".equalsIgnoreCase(cmd)) {
                    running = false;
                    break;
                } else {
                    System.out.println("Unknown command: " + cmd + "  (use 'board' or 'quit')");
                }
            }

            // small sleep to avoid busy loop
            Thread.sleep(100);
        }

        input.stop();
        System.out.println("Exiting.");
    }
}
