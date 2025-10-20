package RfC.components;
import RfC.core.World;
import RfC.core.Component;
import RfC.core.Entity;

public interface EventEffectComponent extends Component {
    // Apply event to world and optionally the active player
    void apply(World world, Entity activePlayer);
}
