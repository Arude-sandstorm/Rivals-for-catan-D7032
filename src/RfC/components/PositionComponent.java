package RfC.components;

import RfC.core.Component;
public class PositionComponent implements Component {
    public int row;
    public int col;

    public PositionComponent(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
