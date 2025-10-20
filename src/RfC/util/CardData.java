package RfC.util;
import com.google.gson.annotations.SerializedName;

public class CardData {
    public String name;
    public String germanName;
    public String type;
    public String placement;
    public String number;          // changed from int → String
    public String sunSymbol;       // changed from int → String
    public String oneOf;
    public String theme;
    public String cost;
    public String victoryPoints;   // changed from Integer → String
    public String CP;
    public String SP;
    public String FP;
    public String PP;
    public String LP;
    public String KP;
    public String Requires;
    public String cardText;
    public String protectionOrRemoval;
}
