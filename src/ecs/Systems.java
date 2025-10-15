package ecs;

import ecs.ECSCore.*;
import ecs.Components.*;

import java.util.*;

public class Systems {

    // --- Produces 1 resource from each region owned by each player ---
    public static class ProductionSystem extends SystemBase {
        @Override
        public void update(World world) {
            for (Entity region : world.getEntitiesWith(RegionComponent.class, OwnerComponent.class)) {
                var reg = world.getComponent(region, RegionComponent.class);
                var owner = world.getComponent(region, OwnerComponent.class).owner;
                var inv = world.getComponent(owner, ResourceInventoryComponent.class);
                inv.add(reg.produces, 1);
                System.out.println(" -> " + world.getComponent(owner, PlayerComponent.class).name +
                                   " gains 1 " + reg.produces);
            }
        }
    }

    // --- Lets each player try to buy a card if they can afford it ---
    public static class CardPlaySystem extends SystemBase {
        private final List<Entity> cardsToPlay;
        public CardPlaySystem(List<Entity> cards) { this.cardsToPlay = cards; }

        @Override
        public void update(World world) {
            for (Entity card : cardsToPlay) {
                var cardData = world.getComponent(card, CardComponent.class);
                var owner = world.getComponent(card, OwnerComponent.class).owner;
                var inv = world.getComponent(owner, ResourceInventoryComponent.class);
                var vp = world.getComponent(owner, VictoryPointsComponent.class);
                if (inv.spend(cardData.cost)) {
                    vp.points += cardData.vp;
                    System.out.println(" -> " + world.getComponent(owner, PlayerComponent.class).name +
                            " plays " + cardData.name + " (+ " + cardData.vp + " VP)");
                }
            }
        }
    }

    // --- Checks win condition ---
    public static class VictorySystem extends SystemBase {
        private final int WIN_POINTS;
        public VictorySystem(int winPoints) { this.WIN_POINTS = winPoints; }

        @Override
        public void update(World world) {
            for (Entity player : world.getEntitiesWith(PlayerComponent.class, VictoryPointsComponent.class)) {
                var vp = world.getComponent(player, VictoryPointsComponent.class);
                var pc = world.getComponent(player, PlayerComponent.class);
                if (vp.points >= WIN_POINTS) {
                    System.out.println("🏆 " + pc.name + " wins the game with " + vp.points + " points!");
                }
            }
        }
    }
}
