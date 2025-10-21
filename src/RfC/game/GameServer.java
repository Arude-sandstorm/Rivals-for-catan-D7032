package RfC.game;

import RfC.components.DeckComponent;
import RfC.components.DiceComponent;
import RfC.components.PlayerComponent;
import RfC.core.Entity;
import RfC.core.World;
import RfC.systems.*;
import RfC.util.ConsoleRenderer;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private static final int PORT = 5555;
    private final ServerSocket serverSocket;
    private final List<Socket> clients = new ArrayList<>();
    private final List<PrintWriter> outs = new ArrayList<>();
    private final List<BufferedReader> ins = new ArrayList<>();

    public GameServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
    }

    private void acceptClients(int n) throws IOException {
        System.out.println("Waiting for " + n + " clients...");
        while (clients.size() < n) {
            Socket s = serverSocket.accept();
            clients.add(s);
            outs.add(new PrintWriter(s.getOutputStream(), true));
            ins.add(new BufferedReader(new InputStreamReader(s.getInputStream())));
            System.out.println("Client connected (" + clients.size() + "/" + n + ")");
        }
    }

    public void startGame() throws Exception {
        acceptClients(2);

        World world = new World();
        DeckComponent[] stacks = new DeckComponent[]{
                new DeckComponent("Stack 1"),
                new DeckComponent("Stack 2"),
                new DeckComponent("Stack 3"),
                new DeckComponent("Stack 4")
        };

        PlayerIOSystem io = new PlayerIOSystem();
        Entity p1 = Game.createPlayer(world, "Alice", false, stacks);
        Entity p2 = Game.createPlayer(world, "Bob", true, stacks);
        List<Entity> players = List.of(p1, p2);

        int current = 0;
        boolean running = true;
        DiceSystem diceSystem = new DiceSystem();
        EventSystem eventSystem = new EventSystem(new EventDeckSystem(new DeckComponent("Events")),
                new EventCardEffectSystem());
        ProductionSystem productionSystem = new ProductionSystem();
        VictorySystem victorySystem = new VictorySystem();

        while (running) {
            Entity active = players.get(current);
            PlayerComponent pc = active.getComponent(PlayerComponent.class);
            PrintWriter out = outs.get(current);
            BufferedReader in = ins.get(current);

            // Display board
            out.println("BOARD|" + new ConsoleRenderer().renderPrincipality(active, world));

            // Dice rolls
            DiceComponent dice = new DiceComponent();
            dice.productionRoll = new Random().nextInt(6) + 1;
            dice.eventRoll = new Random().nextInt(6) + 1;
            out.println("INFO|EventDie → " + dice.eventRoll + "  ProdDie → " + dice.productionRoll);

            // Apply event + production
            if (dice.eventRoll == 1) { eventSystem.update(world.getEntities()); productionSystem.update(world.getEntities()); }
            else { productionSystem.update(world.getEntities()); eventSystem.update(world.getEntities()); }

            // Simple choice: end turn immediately (to keep short)
            out.println("PROMPT|Press Enter to end your turn|ok");
            in.readLine();

            victorySystem.update(world.getEntities());
            if (pc.victoryPoints >= 7) { out.println("INFO|You win!"); running = false; }
            current = (current + 1) % 2;
        }
        for (Socket s : clients) s.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws Exception {
        new GameServer().startGame();
    }
}
