package RfC.systems;

import RfC.components.PlayerComponent;
import RfC.core.Entity;

import java.util.List;
import java.util.List;

public class VictorySystem extends GameSystem {

    private final int WIN_THRESHOLD = 7; // base game victory condition

    @Override
    public void update(List<Entity> entities) {
        for (Entity e : entities) {
            if (!e.hasComponent(PlayerComponent.class)) continue;

            PlayerComponent pc = e.getComponent(PlayerComponent.class);
            int total = calculateTotalPoints(pc);

            if (total >= WIN_THRESHOLD) {
                System.out.println("====================================");
                System.out.println("🏆 " + pc.name + " wins the game!");
                System.out.println("Final score breakdown:");
                System.out.printf("  Victory:  %d%n", pc.victoryPoints);
                System.out.printf("  Commerce: %d%n", pc.commercePoints);
                System.out.printf("  Progress: %d%n", pc.progressPoints);
                System.out.printf("  Strength: %d%n", pc.strengthPoints);
                System.out.printf("  Skill:    %d%n", pc.skillPoints);
                System.out.println("  ------------------");
                System.out.printf("  TOTAL:    %d points%n", total);
                System.out.println("====================================");
            }
        }
    }

    /** Calculates the player’s total score from all point categories. */
    private int calculateTotalPoints(PlayerComponent pc) {
        int sum = pc.victoryPoints
                + pc.commercePoints
                + pc.progressPoints
                + pc.strengthPoints
                + pc.skillPoints;
        return sum;
    }
}
