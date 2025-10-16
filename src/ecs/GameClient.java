package ecs;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameClient {
    public void runClient() throws IOException {
        try (Socket socket = new Socket("localhost", 5000)) {
            var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var out = new PrintWriter(socket.getOutputStream(), true);
            var sc = new Scanner(System.in);
            System.out.println(in.readLine());
            String msg;
            while (true) {
                System.out.print("You: ");
                msg = sc.nextLine();
                out.println(msg);
                System.out.println("Server: " + in.readLine());
            }
        }
    }
}
