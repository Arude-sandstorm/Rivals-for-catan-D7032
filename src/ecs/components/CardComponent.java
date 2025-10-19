// CardComponent.java
package ecs.components;

public class CardComponent implements Component {
    public String name;
    public String germanName;
    public String type;
    public String placement;
    public Integer number;
    public Integer sunSymbol;
    public String oneOf;
    public String theme;
    public String cost;
    public Integer victoryPoints; // nullable
    public String CP, SP, FP, PP, LP, KP;
    public String Requires;
    public String cardText;
    public String protectionOrRemoval;

    // default constructor needed by Jackson
    public CardComponent() {}
}
