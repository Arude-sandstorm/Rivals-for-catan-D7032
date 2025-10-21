package RfC.components;
import RfC.core.Component;
import RfC.core.Entity;

import java.util.*;

public class HandComponent implements Component {
    public final List<Entity> cards = new ArrayList<>();
    public int maxSize = 3;

    public void add(Entity c) { if (c!=null) cards.add(c); }
    public boolean isFull() { return cards.size() >= maxSize; }
    public int size() { return cards.size(); }
}
