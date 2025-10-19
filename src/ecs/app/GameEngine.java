// GameEngine.java
package ecs.app;

import ecs.World;
import ecs.Entity;
import ecs.components.CardComponent;
import ecs.components.DeckComponent;
import ecs.components.PlayerComponent;
import ecs.util.CardLoader;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GameEngine {
    private final World world = new World();
    private final Random rnd = new Random();

    public void init() throws Exception {
        // load all cards into world (each card as an entity with CardComponent)
        CardLoader.loadCards(Paths.get("src/ecs/cards.json"), world);

        // create center stacks: regions, settlements, cities, roads
        Map<String, DeckComponent> centerStacks = new HashMap<>();
        centerStacks.put("regions", new DeckComponent());
        centerStacks.put("settlements", new DeckComponent());
        centerStacks.put("cities", new DeckComponent());
        centerStacks.put("roads", new DeckComponent());

        for (Entity e : world.allEntities()) {
            CardComponent c = e.get(CardComponent.class);
            if (c == null) continue;
            String t = c.type != null ? c.type.toLowerCase() : "";
            if (t.contains("region")) centerStacks.get("regions").add(e.getId());
            else if (t.contains("settlement")) centerStacks.get("settlements").add(e.getId());
            else if (t.contains("city")) centerStacks.get("cities").add(e.getId());
            else if (t.contains("road")) centerStacks.get("roads").add(e.getId());
        }

        // shuffle center stacks
        centerStacks.values().forEach(d -> d.shuffle(rnd));

        // create basic draw stacks (split basic cards into 4 stacks)
        List<Integer> basicCards = world.allEntities().stream()
                .filter(e -> {
                    CardComponent c = e.get(CardComponent.class);
                    return c != null && (c.theme != null && (c.theme.toLowerCase().contains("basic") || c.type.toLowerCase().contains("action") || c.type.toLowerCase().contains("building")));
                })
                .map(Entity::getId)
                .collect(Collectors.toList());
        Collections.shuffle(basicCards, rnd);
        List<DeckComponent> drawStacks = new ArrayList<>();
        for (int i=0;i<4;i++) drawStacks.add(new DeckComponent());
        for (int i=0;i<basicCards.size();i++) drawStacks.get(i%4).add(basicCards.get(i));
        drawStacks.forEach(d -> d.shuffle(rnd));

        // create event stack (simple)
        DeckComponent eventStack = new DeckComponent();
        world.allEntities().stream()
                .filter(e -> {
                    CardComponent c = e.get(CardComponent.class);
                    return c != null && "event".equalsIgnoreCase(c.type);
                })
                .map(Entity::getId)
                .forEach(eventStack::add);
        // shuffle and move Yule card to 4th from bottom (simple implementation)
        eventStack.shuffle(rnd);
        // TODO: implement move-Yule-to-4th-from-bottom rule by locating the Yule card id and repositioning it.

        // create player entities
        Entity p1 = world.createEntity(); p1.add(new PlayerComponent());
        Entity p2 = world.createEntity(); p2.add(new PlayerComponent());
        // initial draw: each player draws 3 from a chosen draw stack (choose stack 0 for demo)
        for (int i=0;i<3;i++) {
            Integer id = drawStacks.get(0).draw();
            if (id != null) p1.get(PlayerComponent.class).hand.add(id);
            id = drawStacks.get(1).draw();
            if (id != null) p2.get(PlayerComponent.class).hand.add(id);
        }

        // TODO: add systems to world (production, event, action, turn, win)
    }

    public World getWorld() { return world; }

    public static void main(String[] args) throws Exception {
        GameEngine g = new GameEngine();
        g.init();
        System.out.println("Initialized world with entities: " + g.getWorld().allEntities().size());
    }
}
