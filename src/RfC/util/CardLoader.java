//// java
//package RfC.util;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import RfC.core.World;
//import RfC.core.Entity;
//import RfC.components.CardComponent;
//
//import java.io.FileReader;
//import java.io.Reader;
//import java.lang.reflect.Type;
//import java.nio.file.Path;
//import java.util.List;
//
//public class CardLoader {
//    public static void loadCards(Path jsonFile, World world) throws Exception {
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<CardComponent>>(){}.getType();
//        try (Reader reader = new FileReader(jsonFile.toFile())) {
//            List<CardComponent> cards = gson.fromJson(reader, listType);
//            if (cards != null) {
//                for (CardComponent card : cards) {
//                    Entity e = world.createEntity();
//                    e.add(card);
//                }
//            }
//        }
//    }
//}
