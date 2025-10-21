package RfC.components;

import RfC.core.Component;

public class RegionComponent implements Component {
    public int diceNumber;
    public ResourceType produces;
    public int stored = 0;    // how many produced but unspent (optional)
    public int cap = 3;       // rule cap

    public RegionComponent(int diceNumber, ResourceType produces) {
        this.diceNumber = diceNumber;
        this.produces = produces;
    }
}
