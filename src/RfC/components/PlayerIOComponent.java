package RfC.components;
import RfC.core.Component;

import java.util.Scanner;

public class PlayerIOComponent implements Component {
    public transient Scanner input; // transient so it's not serialized later
    public String lastMessage = "";

    public PlayerIOComponent() {
        this.input = new Scanner(System.in);
    }
}
