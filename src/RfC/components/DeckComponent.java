// DeckComponent.java
package RfC.components;

import RfC.core.Component;

import java.util.*;

public class DeckComponent implements Component {
    public final Deque<Integer> stack = new ArrayDeque<>();

    public DeckComponent() {}
    public void shuffle(Random rnd) {
        List<Integer> tmp = new ArrayList<>(stack);
        Collections.shuffle(tmp, rnd);
        stack.clear();
        tmp.forEach(stack::addLast);
    }
    public Integer draw() { return stack.pollFirst(); }
    public void putBottom(Integer cardId) { stack.addLast(cardId); }
    public void add(Integer cardId) { stack.addLast(cardId); }
    public int size() { return stack.size(); }
}




