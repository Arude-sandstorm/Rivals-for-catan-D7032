//// java
//package RfC.util;
//
//import RfC.components.PrincipalityComponent;
//import RfC.components.PlayerComponent;
//import RfC.components.CardComponent;
//
//import java.util.function.IntFunction;
//
///**
// * Print helper for an ECS principality grid.
// * Usage:
// *   String s = PrincipalityPrinter.printPrincipality(
// *       principalityComponent,
// *       playerComponent,
// *       id -> /* resolve id -> CardComponent using your World/Entity API *\/ );
// */
//public class PrincipalityPrinter {
//
//    public static String printPrincipality(PrincipalityComponent pc,
//                                           PlayerComponent player,
//                                           IntFunction<CardComponent> resolver) {
//        StringBuilder sb = new StringBuilder();
//        int rows = pc.rows();
//        int cols = pc.cols();
//
//        // column widths
//        int[] w = new int[cols];
//        int minW = 10;
//        for (int c = 0; c < cols; c++) {
//            int m = minW;
//            for (int r = 0; r < rows; r++) {
//                CardComponent card = resolve(pc.get(r, c), resolver);
//                String title = cellTitle(card);
//                String info = cellInfo(card);
//                m = Math.max(m, safeLen(title));
//                m = Math.max(m, safeLen(info));
//            }
//            w[c] = m;
//        }
//
//        // top headers
//        sb.append("      ");
//        for (int c = 0; c < cols; c++) {
//            String hdr = "Col " + c;
//            sb.append(padRight(hdr, w[c] + 3));
//        }
//        sb.append("\n");
//
//        // top border
//        sb.append("    ").append(buildSep(w)).append("\n");
//
//        for (int r = 0; r < rows; r++) {
//            // title line
//            sb.append(String.format("%2d  ", r)).append("|");
//            for (int c = 0; c < cols; c++) {
//                CardComponent card = resolve(pc.get(r, c), resolver);
//                String title = cellTitle(card);
//                sb.append(" ").append(padRight(title, w[c])).append(" ").append("|");
//            }
//            sb.append("\n");
//
//            // info line
//            sb.append("    ").append("|");
//            for (int c = 0; c < cols; c++) {
//                CardComponent card = resolve(pc.get(r, c), resolver);
//                String info = cellInfo(card);
//                sb.append(" ").append(padRight(info, w[c])).append(" ").append("|");
//            }
//            sb.append("\n");
//
//            sb.append("    ").append(buildSep(w)).append("\n");
//        }
//
//        sb.append("\nPoints: ")
//                .append("VP=").append(player == null ? "0" : player.victoryPoints)
//                .append("  CP=").append(player == null ? "0" : player.commercePoints)
//                .append("  Stars=").append(player == null ? "0" : player.stars)
//                .append("  Strength=").append(player == null ? "0" : player.strengthPoints)
//                .append("  Gold=").append(player == null ? "0" : player.gold)
//                .append("\n");
//
//        return sb.toString();
//    }
//
//    private static CardComponent resolve(Integer id, IntFunction<CardComponent> resolver) {
//        if (id == null) return null;
//        try {
//            return resolver.apply(id);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private static int safeLen(String s) {
//        return s == null ? 0 : s.length();
//    }
//
//    private static String buildSep(int[] w) {
//        StringBuilder sep = new StringBuilder();
//        sep.append("+");
//        for (int c = 0; c < w.length; c++) {
//            sep.append("-".repeat(Math.max(0, w[c] + 2)));
//            sep.append("+");
//        }
//        return sep.toString();
//    }
//
//    private static String padRight(String s, int w) {
//        if (s == null) s = "";
//        if (s.length() >= w) return s;
//        return s + " ".repeat(w - s.length());
//    }
//
//    // ----- cell helpers (mirror original behavior) -----
//    private static String cellTitle(CardComponent c) {
//        if (c == null) return "";
//        String title = safeString(c.name);
//        if ("Forest".equals(title)) title += " (L):Lumber";
//        else if ("Hill".equals(title)) title += " (B):Brick";
//        else if ("Field".equals(title)) title += " (G):Grain";
//        else if ("Pasture".equals(title)) title += " (W):Wool";
//        else if ("Mountain".equals(title)) title += " (O):Ore";
//        else if ("Gold Field".equals(title)) title += " (A):Gold";
//        return title;
//    }
//
//    private static String cellInfo(CardComponent c) {
//        if (c == null) return "";
//        // Regions: show dice + stored (0..3)
//        if ("Region".equalsIgnoreCase(safeString(c.type))) {
//            String die = (c.diceRoll <= 0 ? "-" : String.valueOf(c.diceRoll));
//            int stored = Math.max(0, Math.min(3, c.regionProduction));
//            return "d" + die + "  " + stored + "/3";
//        }
//
//        String nm = safeString(c.name);
//        if (c.type != null && c.type.toLowerCase().contains("trade ship")) {
//            if (!nm.equalsIgnoreCase("Large Trade Ship") && nm.endsWith("Ship")) {
//                String res = firstWord(nm);
//                return "2:1 " + res;
//            } else if (nm.equalsIgnoreCase("Large Trade Ship")) {
//                return "LTS (left/right swap 2→1)";
//            }
//        }
//
//        if ("Building".equalsIgnoreCase(safeString(c.type)) &&
//                "Settlement/City Expansions".equalsIgnoreCase(safeString(c.placement))) {
//            if (nm.endsWith("Foundry")) return "Boosts Ore x2 on match";
//            if (nm.endsWith("Mill"))    return "Boosts Grain x2 on match";
//            if (nm.endsWith("Camp"))    return "Boosts Lumber x2 on match";
//            if (nm.endsWith("Factory")) return "Boosts Brick x2 on match";
//            if (nm.endsWith("Shop"))    return "Boosts Wool x2 on match";
//        }
//
//        if ("Road".equalsIgnoreCase(nm)) return "Center";
//        if ("Settlement".equalsIgnoreCase(nm)) return "Center";
//        if ("City".equalsIgnoreCase(nm)) return "Center";
//
//        String pts = summarizePoints(c);
//        if (!pts.isEmpty()) return pts;
//
//        String pl = safeString(c.placement);
//        String tp = safeString(c.type);
//        if (!pl.isEmpty() || !tp.isEmpty()) return (pl + " " + tp).trim();
//        return "";
//    }
//
//    private static String summarizePoints(CardComponent c) {
//        if (c == null) return "";
//        int vp = parseIntSafe(c.victoryPoints);
//        int cp = parseIntSafe(c.CP);
//        int sp = parseIntSafe(c.SP);
//        int fp = parseIntSafe(c.FP);
//        int pp = parseIntSafe(c.PP);
//
//        StringBuilder t = new StringBuilder();
//        if (vp > 0 || cp > 0 || sp > 0 || fp > 0 || pp > 0) {
//            t.append("[");
//            if (vp > 0) t.append("VP").append(vp).append(" ");
//            if (cp > 0) t.append("CP").append(cp).append(" ");
//            if (sp > 0) t.append("SP").append(sp).append(" ");
//            if (fp > 0) t.append("FP").append(fp).append(" ");
//            if (pp > 0) t.append("PP").append(pp).append(" ");
//            if (t.charAt(t.length() - 1) == ' ') t.deleteCharAt(t.length() - 1);
//            t.append("]");
//        }
//        return t.toString();
//    }
//
//    private static String firstWord(String s) {
//        if (s == null) return "";
//        String[] toks = s.trim().split("\\s+");
//        return toks.length == 0 ? "" : toks[0];
//    }
//
//    private static int parseIntSafe(String s) {
//        if (s == null || s.isBlank()) return 0;
//        try { return Integer.parseInt(s.trim()); }
//        catch (Exception e) { return 0; }
//    }
//
//    private static String safeString(Object o) {
//        return o == null ? "" : String.valueOf(o);
//    }
//}
