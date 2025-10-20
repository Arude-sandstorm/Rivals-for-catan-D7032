package RfC.components;

import RfC.core.Component;

public class RegionComponent implements Component {
    public int diceNumber;
    public ResourceType produces;

    public RegionComponent(int diceNumber, ResourceType produces) {
        this.diceNumber = diceNumber;
        this.produces = produces;
    }
}
