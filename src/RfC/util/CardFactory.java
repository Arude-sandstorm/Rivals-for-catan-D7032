package RfC.util;
import RfC.core.Entity;

import java.util.ArrayList;
import java.util.List;

public class CardFactory {
    // In a full implementation, this would load cards from cards.json (gson) and create Entities with CardComponents.
    public static List<Entity> createSimpleRegionCards() {
        List<Entity> list = new ArrayList<>();
        // Create two simple region-holder players (for demo)
        return list;
    }
}
