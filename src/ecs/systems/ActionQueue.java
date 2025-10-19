//java
package ecs.systems;

import ecs.net.NetworkMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Thread-safe queue for NetworkMessage ACTIONs.
 * Producers: InputSystem, NetworkSystem.
 * Consumer: main game loop / TurnSystem.
 */
public class ActionQueue {
    private final BlockingQueue<NetworkMessage> q = new LinkedBlockingQueue<>();

    public void enqueue(NetworkMessage m) {
        if (m == null) return;
        q.offer(m);
    }

    // Non-blocking drain for main loop ticks
    public List<NetworkMessage> drainAll() {
        List<NetworkMessage> out = new ArrayList<>();
        q.drainTo(out);
        return out;
    }

    // Blocking take if desired
    public NetworkMessage take() throws InterruptedException {
        return q.take();
    }
}
