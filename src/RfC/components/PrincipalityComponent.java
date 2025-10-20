// java
package RfC.components;

import RfC.core.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a dynamic 2D grid of card entity ids (Integer), null = empty cell.
 */
public class PrincipalityComponent implements Component {
    public final List<List<Integer>> grid = new ArrayList<>();

    public PrincipalityComponent() {
        // start with a 5x5 empty grid like the original implementation
        for (int r = 0; r < 5; r++) {
            List<Integer> row = new ArrayList<>();
            for (int c = 0; c < 5; c++) row.add(null);
            grid.add(row);
        }
    }

    public Integer get(int r, int c) {
        if (r < 0 || c < 0) return null;
        if (r >= grid.size()) return null;
        List<Integer> row = grid.get(r);
        if (row == null || c >= row.size()) return null;
        return row.get(c);
    }

    public void place(int r, int c, Integer cardEntityId) {
        ensureSize(r, c);
        grid.get(r).set(c, cardEntityId);
    }

    public int expandAfterEdgeBuild(int col) {
        int cols = grid.get(0).size();
        if (col == 0) {
            for (List<Integer> row : grid) row.add(0, null);
            col += 1;
        } else if (col == cols - 1) {
            for (List<Integer> row : grid) row.add(null);
        }
        return col;
    }

    private void ensureSize(int r, int c) {
        while (grid.size() <= r) {
            List<Integer> row = new ArrayList<>();
            int cols = grid.isEmpty() ? 5 : grid.get(0).size();
            for (int i = 0; i < cols; i++) row.add(null);
            grid.add(row);
        }
        for (List<Integer> row : grid) {
            while (row.size() <= c) row.add(null);
        }
    }

    public int rows() {
        return grid.size();
    }

    public int cols() {
        return grid.isEmpty() ? 0 : grid.get(0).size();
    }

    public boolean hasInPrincipality(java.util.function.IntPredicate matchId) {
        for (List<Integer> row : grid) {
            for (Integer id : row) {
                if (id != null && matchId.test(id)) return true;
            }
        }
        return false;
    }
}
