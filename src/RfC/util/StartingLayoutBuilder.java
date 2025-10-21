package RfC.util;
import RfC.components.*;
import RfC.core.CardType;
import RfC.core.Entity;

import java.util.function.BiConsumer;

public final class StartingLayoutBuilder {

    private StartingLayoutBuilder() {}

    /**
     * Build the introductory principality exactly like the reference board:
     *
     *   Row 1: [c0]=Forest(L):Lumber , [c2]=Gold Field(A):Gold , [c4]=Field(G):Grain
     *   Row 2: [c1]=Settlement , [c2]=Road , [c3]=Settlement
     *   Row 3: [c0]=Hill(B):Brick , [c2]=Pasture(W):Wool , [c4]=Mountain(O):Ore
     *
     * If mirror==true, columns are mirrored (useful for player 2 if you want symmetry).
     */
    public static void buildIntroPrincipality(Entity player, BoardComponent board, boolean mirror) {
        PlayerComponent pc = player.getComponent(PlayerComponent.class);
        board.rows = 5; board.cols = 5;

        int c0 = col(mirror,0,5), c1 = col(mirror,1,5),
                c2 = col(mirror,2,5), c3 = col(mirror,3,5), c4 = col(mirror,4,5);

        // centers
        placeSettlement(player, board, 2, c1);
        placeRoad(player, board, 2, c2);
        placeSettlement(player, board, 2, c3);
        pc.victoryPoints += 2;

        // region dice positions (fixed like the picture)
        // Row1: (1,c0)=d3, (1,c2)=d4, (1,c4)=d5
        // Row3: (3,c0)=d2, (3,c2)=d1, (3,c4)=d6

        // Build a region-type pool per rule §2c (intro game typical mix)
        // Example pool counts — adjust to your exact sheet if needed:
        // Field: 3,1  Mountain: 4,2  Hill: 5,1  Forest: 6,2  Pasture: 6,5  Gold: 3,2
        // We'll pick six types from a sensible subset and shuffle.
        java.util.List<ResourceType> pool = new java.util.ArrayList<>();
        pool.add(ResourceType.WOOD);  pool.add(ResourceType.BRICK);
        pool.add(ResourceType.GOLD);  pool.add(ResourceType.GRAIN);
        pool.add(ResourceType.WOOL);  pool.add(ResourceType.ORE);
        java.util.Collections.shuffle(pool);

        // map resource types to names
        java.util.function.Function<ResourceType,String> nameOf = rt -> switch (rt) {
            case WOOD -> "Forest (L):Lumber";
            case BRICK -> "Hill (B):Brick";
            case GOLD -> "Gold Field (A):Gold";
            case GRAIN -> "Field (G):Grain";
            case WOOL -> "Pasture (W):Wool";
            case ORE -> "Mountain (O):Ore";
            default -> "Region";
        };

        // TOP row (row 1) d3,d4,d5
        placeRegion(player, board, nameOf.apply(pool.get(0)), 3, pool.get(0), 1, 1, c0);
        placeRegion(player, board, nameOf.apply(pool.get(1)), 4, pool.get(1), 1, 1, c2);
        placeRegion(player, board, nameOf.apply(pool.get(2)), 5, pool.get(2), 1, 1, c4);

        // BOTTOM row (row 3) d2,d1,d6
        placeRegion(player, board, nameOf.apply(pool.get(3)), 2, pool.get(3), 1, 3, c0);
        placeRegion(player, board, nameOf.apply(pool.get(4)), 1, pool.get(4), 1, 3, c2);
        placeRegion(player, board, nameOf.apply(pool.get(5)), 6, pool.get(5), 1, 3, c4);
    }


    private static int col(boolean mirror, int c, int totalCols) {
        if (!mirror) return c;
        // mirror within 0..4 (5 columns)
        return (4 - c);
    }

    private static void placeSettlement(Entity player, BoardComponent board, int row, int col) {
        Entity s = new Entity();
        s.addComponent(new CardComponent("Settlement", CardType.SETTLEMENT));
        s.addComponent(new PositionComponent(row, col));
        board.addEntityAt(s, row, col);
    }

    private static void placeRoad(Entity player, BoardComponent board, int row, int col) {
        Entity r = new Entity();
        r.addComponent(new CardComponent("Road", CardType.ROAD));
        r.addComponent(new PositionComponent(row, col));
        board.addEntityAt(r, row, col);
    }

    private static void placeRegion(Entity player, BoardComponent board,
                                    String name, int diceNumber, ResourceType produces,
                                    int cap /*unused now but future-proof*/, int row, int col) {
        Entity region = new Entity();
        region.addComponent(new CardComponent(name, CardType.REGION));
        region.addComponent(new RegionComponent(diceNumber, produces));
        region.addComponent(new PositionComponent(row, col));
        board.addEntityAt(region, row, col);
    }
}
