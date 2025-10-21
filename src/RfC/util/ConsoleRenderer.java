package RfC.util;
import RfC.components.*;
import RfC.core.Entity;
import RfC.core.World;

import java.util.*;
import java.util.stream.Collectors;
import java.util.*;
import java.util.stream.Collectors;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleRenderer {

    public String renderPrincipality(Entity player, World world) {
        PlayerComponent pc = player.getComponent(PlayerComponent.class);
        BoardComponent board = player.getComponent(BoardComponent.class);
        if (board == null) return "[No board assigned]\n";

        // Compute max width for each column based on contents
        int[] colWidths = new int[board.cols];
        int minW = 14;

        for (int c = 0; c < board.cols; c++) {
            int maxW = minW;
            for (int r = 0; r < board.rows; r++) {
                Entity cell = board.getEntityAt(r, c);
                if (cell != null && cell.hasComponent(CardComponent.class)) {
                    CardComponent cc = cell.getComponent(CardComponent.class);
                    String title = cc.name;
                    String info = getInfoLine(cell);
                    maxW = Math.max(maxW, Math.max(title.length(), info.length()));
                }
            }
            colWidths[c] = maxW;
        }

        StringBuilder sb = new StringBuilder();

        // Column headers
        sb.append("      ");
        for (int c = 0; c < board.cols; c++) {
            sb.append(padRight("Col " + c, colWidths[c] + 3));
        }
        sb.append("\n");

        sb.append("    ").append(buildSeparator(colWidths)).append("\n");

        // Rows
        for (int r = 0; r < board.rows; r++) {
            // Title line
            sb.append(String.format("%2d  ", r)).append("|");
            for (int c = 0; c < board.cols; c++) {
                Entity cell = board.getEntityAt(r, c);
                String title = cellTitle(cell);
                sb.append(" ").append(padRight(title, colWidths[c])).append(" |");
            }
            sb.append("\n");

            // Info line
            sb.append("    ").append("|");
            for (int c = 0; c < board.cols; c++) {
                Entity cell = board.getEntityAt(r, c);
                String info = getInfoLine(cell);
                sb.append(" ").append(padRight(info, colWidths[c])).append(" |");
            }
            sb.append("\n");

            sb.append("    ").append(buildSeparator(colWidths)).append("\n");
        }

        // Resource summary
        sb.append("\nPoints: VP=").append(pc.victoryPoints)
                .append("  CP=").append(pc.commercePoints)
                .append("  SP=").append(pc.skillPoints)
                .append("  FP=").append(pc.strengthPoints)
                .append("  PP=").append(pc.progressPoints)
                .append("\n");

        return sb.toString();
    }

    public String renderHand(Entity player) {
        HandComponent hand = player.getComponent(HandComponent.class);
        if (hand == null) return "Hand: []\n";
        StringBuilder sb = new StringBuilder();
        sb.append("Hand (").append(hand.size()).append("):\n");
        for (int i=0;i<hand.cards.size();i++) {
            CardComponent cc = hand.cards.get(i).getComponent(CardComponent.class);
            sb.append("  [").append(i).append("] ").append(cc.name)
                    .append("  {type: ").append(cc.cardType).append("}\n");
        }
        return sb.toString();
    }

    public String renderResources(PlayerComponent pc) {
        return "Resources: " + pc.resources.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));
    }

    // ---- Helper methods ----

    private String buildSeparator(int[] w) {
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < w.length; c++) {
            sb.append("+").append("-".repeat(w[c] + 2));
        }
        sb.append("+");
        return sb.toString();
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private String cellTitle(Entity e) {
        if (e == null || !e.hasComponent(CardComponent.class)) return "";
        CardComponent cc = e.getComponent(CardComponent.class);
        return cc.name;
    }

    private String getInfoLine(Entity e) {
        if (e == null) return "";
        if (e.hasComponent(RegionComponent.class)) {
            RegionComponent rc = e.getComponent(RegionComponent.class);
            return "d" + rc.diceNumber + " " + rc.produces + " 1/3";
        }
        return "";
    }



}
