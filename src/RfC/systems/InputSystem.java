////java
//package RfC.systems;
//
//import com.google.gson.Gson;
//import RfC.core.World;
//import RfC.core.Entity;
//import RfC.components.NetworkComponent;
//import RfC.net.NetworkMessage;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Reads CLI lines and emits ACTION messages.
// * If the provided local player entity carries a client NetworkComponent (remote=false),
// * the message is serialized and sent to the server. Otherwise the message is enqueued locally.
// */
//public class InputSystem {
//    private final World world;
//    private final int localPlayerEntityId;
//    private final ActionQueue actionQueue;
//    private final Gson gson = new Gson();
//    private final ExecutorService reader = Executors.newSingleThreadExecutor();
//
//    public InputSystem(World world, int localPlayerEntityId, ActionQueue actionQueue) {
//        this.world = world;
//        this.localPlayerEntityId = localPlayerEntityId;
//        this.actionQueue = actionQueue;
//    }
//
//    public void start() {
//        reader.submit(this::readLoop);
//    }
//
//    public void stop() {
//        reader.shutdownNow();
//    }
//
//    private void readLoop() {
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
//            String line;
//            while (!Thread.currentThread().isInterrupted() && (line = br.readLine()) != null) {
//                line = line.trim();
//                if (line.isEmpty()) continue;
//
//                NetworkMessage msg = new NetworkMessage();
//                msg.type = "ACTION";
//                Map<String, Object> payload = new HashMap<>();
//                payload.put("cmd", line);
//                msg.payload = payload;
//
//                // world.getEntity(...) returns Optional<Entity> -> unwrap to nullable Entity
//                Entity playerEntity = null;
//                try {
//                    playerEntity = world.getEntity(localPlayerEntityId).orElse(null);
//                } catch (Exception ignored) {}
//
//                NetworkComponent nc = null;
//                if (playerEntity != null) {
//                    try { nc = playerEntity.get(NetworkComponent.class); } catch (Exception ignored) {}
//                }
//
//                if (nc != null && !nc.remote) {
//                    // client mode -> send to server
//                    nc.sendJson(gson.toJson(msg));
//                } else {
//                    // local/authoritative -> enqueue for processing
//                    actionQueue.enqueue(msg);
//                }
//            }
//        } catch (Exception ignored) {
//        }
//    }
//}
