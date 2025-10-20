package RfC.systems;
import RfC.components.PlayerComponent;
import RfC.core.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TurnSystem extends GameSystem {
    private final List<Entity> players = new ArrayList<>();
    private int currentPlayerIndex = 0;

    public TurnSystem(List<Entity> players) {
        this.players.addAll(players);
    }

    @Override
    public void update(List<Entity> entities) {
        if (players.isEmpty()) return;
        Entity active = players.get(currentPlayerIndex);
        System.out.println("--- Turn for " + active.getComponent(PlayerComponent.class).name + " ---");
        // Trigger dice & event/production via systems (they will react to active player via world state)
        // The World update order should ensure DiceSystem and EventSystem run before ProductionSystem.
        // Here we simply let the world call all systems in order.
        // After the whole round we check victory via VictorySystem (which is a separate system).
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}
