//// CardComponent.java
package RfC.components;
//
//public class CardComponent implements Component {
//    public String name;
//    public String germanName;
//    public String type;
//    public String placement;
//    public Integer number;
//    public Integer sunSymbol;
//    public String oneOf;
//    public String theme;
//    public String cost;
//    public Integer victoryPoints; // nullable
//    public String CP, SP, FP, PP, LP, KP;
//    public String Requires;
//    public String cardText;
//    public String protectionOrRemoval;
//
//    // default constructor needed by Jackson
//    public CardComponent() {}
//}

//
//// java
//package RfC.components;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import RfC.core.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.reflect.Type;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//public class CardComponent implements Component {
//    private List<Card> cards;
//
//    public void loadFromClasspath(String resourcePath) throws IOException {
//        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
//            if (is == null) throw new IOException("Resource not found: " + resourcePath);
//            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
//                Type listType = new TypeToken<List<Card>>() {}.getType();
//                this.cards = new Gson().fromJson(reader, listType);
//            }
//        }
//    }
//
//    public List<Card> getCards() {
//        return cards;
//    }
//
//    // example usage:
//    // new CardComponent().loadFromClasspath("/cards.json");
//}
import RfC.core.CardType;
import RfC.core.Component;

public class CardComponent implements Component {
    public String name;
    public CardType cardType;

    public CardComponent(String name, CardType type) {
        this.name = name; this.cardType = type;
    }
}


