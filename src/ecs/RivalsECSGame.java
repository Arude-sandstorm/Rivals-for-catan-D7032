package ecs;

import ecs.ECSCore.*;
import ecs.Components.*;
import ecs.Systems.*;
import java.util.*;

public class RivalsECSGame {

    public static void main(String[] args) {
        World world = new World();

        // --- Create players ---
        Entity p1 = world.createEntity();
        world.addComponent(p1, new PlayerComponent("Alice"));
        world.addComponent(p1, new ResourceInventoryComponent());
        world.addComponent(p1, new VictoryPointsComponent());

        Entity p2 = world.createEntity();
        world.addComponent(p2, new PlayerComponent("Bob"));
        world.addComponent(p2, new ResourceInventoryComponent());
        world.addComponent(p2, new VictoryPointsComponent());

        // --- Create regions with dice numbers ---
        Entity r1 = world.createEntity();
        world.addComponent(r1, new RegionComponent(ResourceType.WOOD, 6));
        world.addComponent(r1, new OwnerComponent(p1));

        Entity r2 = world.createEntity();
        world.addComponent(r2, new RegionComponent(ResourceType.BRICK, 8));
        world.addComponent(r2, new OwnerComponent(p2));

        // --- Create a few cards (buildings) ---
        Map<ResourceType, Integer> roadCost = Map.of(ResourceType.WOOD, 1, ResourceType.BRICK, 1);
        Entity roadCard1 = world.createEntity();
        world.addComponent(roadCard1, new CardComponent("Road", roadCost, 1));
        world.addComponent(roadCard1, new OwnerComponent(p1));

        Entity roadCard2 = world.createEntity();
        world.addComponent(roadCard2, new CardComponent("Road", roadCost, 1));
        world.addComponent(roadCard2, new OwnerComponent(p2));

        // --- Systems ---
        // Dice rolled first each turn, then production, then card play, then victory check
        List<SystemBase> systems = List.of(
                new DiceSystem(),
                new ProductionSystem(),
                new CardPlaySystem(List.of(roadCard1, roadCard2)),
                new VictorySystem(3)
        );

        // --- Simulate turns ---
        for (int turn = 1; turn <= 10; turn++) {
            System.out.println("\n=== Turn " + turn + " ===");
            for (SystemBase s : systems) s.update(world);

            // Print player states
            for (Entity p : world.getEntitiesWith(PlayerComponent.class, ResourceInventoryComponent.class)) {
                var pc = world.getComponent(p, PlayerComponent.class);
                var inv = world.getComponent(p, ResourceInventoryComponent.class);
                var vp = world.getComponent(p, VictoryPointsComponent.class);
                System.out.println(pc.name + " -> " + inv + " | VP: " + vp.points);
            }
        }
    }
}
