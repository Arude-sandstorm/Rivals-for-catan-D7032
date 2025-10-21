// DeckComponent.java
package RfC.components;

import RfC.core.CardType;
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

    public List<Entity> drawN(int n) {
        List<Entity> out = new ArrayList<>();
        for (int i=0;i<n;i++) {
            Entity e = draw();
            if (e==null) break;
            out.add(e);
        }
        return out;
    }
    public static DeckComponent buildEventDeck(List<Entity> allCards) {
        DeckComponent eventDeck = new DeckComponent("Event Deck");
        java.util.List<Entity> others = new java.util.ArrayList<>();
        Entity yule = null;

        for (Entity c : allCards) {
            CardComponent cc = c.getComponent(CardComponent.class);
            if (cc.cardType == CardType.EVENT) {
                if (cc.name.toLowerCase().contains("yule")) yule = c;
                else others.add(c);
            }
        }
        java.util.Collections.shuffle(others);

        // Insert Yule 4th from bottom:
        // bottom = index 0; top = last (or reverse if you prefer)
        for (Entity e : others) eventDeck.add(e);
        if (yule != null) {
            int pos = Math.min(3, eventDeck.size()); // 0-based: 0..3 from bottom
            eventDeck.cards.add(pos, yule);
        }
        return eventDeck;
    }


}




