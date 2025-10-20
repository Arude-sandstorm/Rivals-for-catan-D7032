// java
package RfC.net;

import RfC.core.World;
import RfC.core.Entity;
import RfC.components.NetworkComponent;
import RfC.components.ControlComponent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple network system that can act as server (accept connections) or client (connect).
 * On server: creates an entity per connection and attaches NetworkComponent + ControlComponent(REMOTE).
 * On client: connects and exposes a NetworkComponent on a local Player entity if desired.
 *
 * This is a minimal example: you must route incoming messages into your game systems
 * (for example by writing them to a thread-safe queue or calling a World/Entity command API).
 */
public class NetworkSystem {
    private final World world;
    private final Gson gson = new Gson();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public NetworkSystem(World world) {
        this.world = world;
    }

    // Start server to accept up to `maxConns` remote players (2 in your requirement).
    public void startServer(int port, int maxConns) throws IOException {
        serverSocket = new ServerSocket(port);
        threadPool.submit(() -> {
            try {
                int accepted = 0;
                while (!serverSocket.isClosed() && accepted < maxConns) {
                    Socket s = serverSocket.accept();
                    accepted++;
                    setupRemotePlayer(s);
                }
            } catch (IOException ignored) {}
        });
    }

    // Connect as client to server. Client should have a local Player entity created separately.
    public void startClient(String host, int port, Entity localPlayerEntity) throws IOException {
        clientSocket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

        // attach network component to the provided player entity so TurnSystem can send
        localPlayerEntity.add(new NetworkComponent(clientSocket, in, out, false));

        // listen for messages and dispatch into world (example: log / call handlers)
        threadPool.submit(() -> readerLoop(in, localPlayerEntity));
    }

    private void setupRemotePlayer(Socket s) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

            // create an ECS player entity for this remote connection
            Entity playerEntity = world.createEntity();
            playerEntity.add(new NetworkComponent(s, in, out, true));
            playerEntity.add(new ControlComponent(ControlComponent.Type.REMOTE));

            // start reading messages from client
            threadPool.submit(() -> readerLoop(in, playerEntity));
        } catch (IOException e) {
            try { s.close(); } catch (IOException ignored) {}
        }
    }

    // read loop: parse incoming JSON into NetworkMessage and route to world
    private void readerLoop(BufferedReader in, Entity playerEntity) {
        String line;
        try {
            while ((line = in.readLine()) != null) {
                try {
                    NetworkMessage msg = gson.fromJson(line, NetworkMessage.class);
                    handleMessage(playerEntity, msg);
                } catch (JsonSyntaxException e) {
                    // malformed message, ignore or log
                }
            }
        } catch (IOException ignored) {
        } finally {
            // connection closed: you should remove player entity or mark disconnected
            // Example: world.removeEntity(playerEntity.getId()); // adapt to your World API
        }
    }

    // Dispatch message into ECS world: this should be integrated with your systems (turns, actions)
    private void handleMessage(Entity playerEntity, NetworkMessage msg) {
        if (msg == null || msg.type == null) return;
        switch (msg.type) {
            case "ACTION":
                // payload contains action name/params -> enqueue to game action queue
                // Example: world.enqueueAction(new RemotePlayerAction(playerEntity.getId(), msg.payload));
                break;
            case "JOIN":
                // handle join metadata if needed
                break;
            case "SYNC_STATE":
                // update local world state snapshot (if using authoritative server)
                break;
            default:
                // unknown message type
        }
    }

    public void shutdown() {
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception ignored) {}
        try { if (clientSocket != null) clientSocket.close(); } catch (Exception ignored) {}
        threadPool.shutdownNow();
    }
}
