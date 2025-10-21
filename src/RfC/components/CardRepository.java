//package RfC.components;
//import java.util.*;
//import RfC.core.Entity;
//import RfC.util.CardFactory;
//
//public interface CardRepository {
//    List<Entity> loadAllCards();
//}
//
//public class JsonCardRepository implements CardRepository {
//    private final String jsonPath;
//    public JsonCardRepository(String jsonPath) { this.jsonPath = jsonPath; }
//
//    @Override
//    public List<Entity> loadAllCards() {
//        return CardFactory.loadCardsFromJson(jsonPath);
//    }
//}
