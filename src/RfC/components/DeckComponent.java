// DeckComponent.java
package RfC.components;

import RfC.core.Component;
import RfC.core.Entity;

import java.util.*;

public class DeckComponent implements Component {
    public final Deque<Integer> stack = new ArrayDeque<>();
    public String name;
    public LinkedList<Entity> cards = new LinkedList<>();
    public DeckComponent() {}

//    public void shuffle(Random rnd) {
//        List<Integer> tmp = new ArrayList<>(stack);
//        Collections.shuffle(tmp, rnd);
//        stack.clear();
//        tmp.forEach(stack::addLast);
//    }
    //public Integer draw() { return stack.pollFirst(); }
    public void putBottom(Integer cardId) { stack.addLast(cardId); }
    public void add(Integer cardId) { stack.addLast(cardId); }

    //public int size() { return stack.size(); }
    public DeckComponent(String name) {
        this.name = name;
    }

    public void shuffle() {
        java.util.Collections.shuffle(cards);
    }

    public Entity draw() {
        return cards.isEmpty() ? null : cards.removeFirst();
    }

    public void add(Entity e) {
        cards.add(e);
    }

    public void addAll(java.util.List<Entity> list) {
        cards.addAll(list);
    }

    public void returnToBottom(Entity e) {
        cards.addLast(e);
    }

    public int size() { return cards.size(); }
}




