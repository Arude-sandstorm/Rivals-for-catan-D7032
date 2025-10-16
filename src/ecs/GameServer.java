package ecs;

import java.io.*;
import java.net.*;

public class GameServer {
    public void runServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Waiting for connection on port 5000...");
            Socket client = serverSocket.accept();
            System.out.println("Player connected!");
            var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            var out = new PrintWriter(client.getOutputStream(), true);
            out.println("Welcome to Rivals ECS Server!");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Client: " + line);
                out.println("Echo: " + line);
            }
        }
    }
}
