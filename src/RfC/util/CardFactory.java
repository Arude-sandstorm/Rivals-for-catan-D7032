package RfC.util;
import RfC.components.CardComponent;
import RfC.components.RegionComponent;
import RfC.components.ResourceType;
import RfC.components.VictoryComponent;
import RfC.core.CardType;
import RfC.core.Entity;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CardFactory {

    public static List<Entity> loadCardsFromJson(String filePath) {
        List<Entity> cards = new ArrayList<>();

        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<CardData>>() {}.getType();
            List<CardData> dataList = gson.fromJson(reader, listType);

            for (CardData data : dataList) {
                Entity e = new Entity();

                // Basic card info
                CardComponent card = new CardComponent(data.name, mapCardType(data.type));
                e.addComponent(card);

                // Add victory marker if the card gives VP
                int vp = parseIntSafe(data.victoryPoints);
                if (vp > 0) {
                    VictoryComponent vc = new VictoryComponent();
                    e.addComponent(vc);
                }

                // Region cards (produce resources on a dice number)
                if ("Region".equalsIgnoreCase(data.type)) {
                    int diceNum = parseIntSafe(data.number);
                    ResourceType produces = mapResourceByName(data.name);
                    e.addComponent(new RegionComponent(diceNum, produces));
                }

                cards.add(e);
            }

            System.out.println("Loaded " + cards.size() + " cards from " + filePath);

        } catch (Exception e) {
            System.err.println("Error loading cards from JSON: " + e.getMessage());
        }

        return cards;
    }

    // --- Utility parsing helpers ---

    private static int parseIntSafe(String value) {
        if (value == null) return 0;
        try {
            // Handle cases like "0 - 2" or "3–4"
            value = value.replaceAll("[^0-9-]", ""); // remove non-numeric chars except dash
            if (value.contains("-")) {
                String first = value.split("-")[0].trim();
                return Integer.parseInt(first);
            }
            if (value.isEmpty()) return 0;
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static CardType mapCardType(String type) {
        if (type == null) return CardType.BASIC;
        switch (type.toLowerCase()) {
            case "region": return CardType.REGION;
            case "building": return CardType.BASIC;
            case "event": return CardType.EVENT;
            case "settlement": return CardType.SETTLEMENT;
            case "city": return CardType.CITY;
            case "road": return CardType.ROAD;
            case "action": return CardType.ACTION;
            default: return CardType.BASIC;
        }
    }

    private static ResourceType mapResourceByName(String name) {
        if (name == null) return ResourceType.NONE;
        String lower = name.toLowerCase();
        if (lower.contains("forest")) return ResourceType.WOOD;
        if (lower.contains("hill")) return ResourceType.BRICK;
        if (lower.contains("mountain")) return ResourceType.ORE;
        if (lower.contains("pasture")) return ResourceType.WOOL;
        if (lower.contains("field") || lower.contains("farm")) return ResourceType.GRAIN;
        if (lower.contains("gold")) return ResourceType.GOLD;
        return ResourceType.NONE;
    }
    public static Map<ResourceType,Integer> parseCost(String s) {
        Map<ResourceType,Integer> m = new java.util.EnumMap<>(ResourceType.class);
        if (s == null) return m;
        for (char ch : s.toUpperCase().toCharArray()) {
            switch (ch) {
                case 'L' -> m.merge(ResourceType.WOOD, 1, Integer::sum);
                case 'B' -> m.merge(ResourceType.BRICK,1, Integer::sum);
                case 'W' -> m.merge(ResourceType.WOOL, 1, Integer::sum);
                case 'O' -> m.merge(ResourceType.ORE,  1, Integer::sum);
                case 'G' -> m.merge(ResourceType.GRAIN,1, Integer::sum);
                case 'A' -> m.merge(ResourceType.GOLD, 1, Integer::sum);
            }
        }
        return m;
    }

}
