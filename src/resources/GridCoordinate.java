package resources;

public class GridCoordinate {
    public final int ROW;
    public final int COL;

    public GridCoordinate(int r, int c) {
        ROW = r;
        COL = c;
    }

    @Override
    public String toString() {
        return "(" + ROW + ", " + COL + ")";
    }
}
