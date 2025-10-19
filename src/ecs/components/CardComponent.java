// java
package ecs.components;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CardComponent implements Component {
    private List<Card> cards;

    public void loadFromClasspath(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) throw new IOException("Resource not found: " + resourcePath);
            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                Type listType = new TypeToken<List<Card>>() {}.getType();
                this.cards = new Gson().fromJson(reader, listType);
            }
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    // example usage:
    // new CardComponent().loadFromClasspath("/cards.json");
}
