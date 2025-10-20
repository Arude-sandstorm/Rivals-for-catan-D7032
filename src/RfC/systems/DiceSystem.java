package RfC.systems;
import RfC.components.DiceComponent;
import RfC.core.Entity;
import java.util.List;
import java.util.Random;


public class DiceSystem extends GameSystem {
    private final Random rand = new Random();
    private DiceComponent dice = new DiceComponent();

    @Override
    public void update(List<Entity> entities) {
        dice.productionRoll = rand.nextInt(6) + 1;
        dice.eventRoll = rand.nextInt(6) + 1;
        // store dice on a special singleton entity in world (if present), otherwise keep internal
        Entity diceEntity = world.getEntities().stream().filter(e -> e.hasComponent(DiceComponent.class)).findFirst().orElse(null);
        if (diceEntity == null) {
            diceEntity = new Entity();
            diceEntity.addComponent(dice);
            world.addEntity(diceEntity);
        } else {
            DiceComponent d = diceEntity.getComponent(DiceComponent.class);
            d.productionRoll = dice.productionRoll;
            d.eventRoll = dice.eventRoll;
        }
        System.out.println("Rolled: production=" + dice.productionRoll + " event=" + dice.eventRoll);
    }
}
