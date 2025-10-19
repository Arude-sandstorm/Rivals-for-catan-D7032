// java
package ecs.components;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Attach to a Player entity that is backed by a network connection.
 */
public class NetworkComponent implements Component {
    public final Socket socket;
    public final BufferedReader in;
    public final PrintWriter out;
    public final boolean remote; // true => this entity is controlled over network

    public NetworkComponent(Socket socket, BufferedReader in, PrintWriter out, boolean remote) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.remote = remote;
    }

    public void sendJson(String json) {
        if (out != null) {
            out.println(json);
            out.flush();
        }
    }

    public void closeQuietly() {
        try { if (in != null) in.close(); } catch (Exception ignored) {}
        try { if (out != null) out.close(); } catch (Exception ignored) {}
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
    }
}
