package RfC.game;
import java.io.*;
import java.net.*;

public class GameClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5555);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Connected to server.");
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith("PROMPT|")) {
                String[] parts = line.split("\\|", 3);
                System.out.println(parts[1]);
                if (parts.length == 3) System.out.println(parts[2].replace(",", " / "));
                System.out.print("> ");
                String ans = console.readLine();
                out.println("INPUT|" + ans);
            } else if (line.startsWith("INFO|")) {
                System.out.println(line.substring(5));
            } else if (line.startsWith("BOARD|")) {
                System.out.println("\n=== Board ===\n" + line.substring(6) + "\n==============\n");
            } else {
                System.out.println(line);
            }
        }
        socket.close();
    }
}
