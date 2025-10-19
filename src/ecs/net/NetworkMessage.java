// java
package ecs.net;

import java.util.Map;

/**
 * Generic message envelope. Keep payload small and explicit.
 * Example types: "JOIN", "STATE", "ACTION", "TURN_START", "ROLL"
 */
public class NetworkMessage {
    public String type;
    public Map<String, Object> payload;

    public NetworkMessage() {}
    public NetworkMessage(String type, Map<String, Object> payload) {
        this.type = type;
        this.payload = payload;
    }
}
