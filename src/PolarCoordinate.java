public class PolarCoordinate {
    public final double ANGLE;
    public final double DISTANCE;

    public PolarCoordinate(double a, double d) {
        ANGLE = a;
        DISTANCE = d;
    }

    @Override
    public String toString() {
        return "<" + ANGLE + ", " + DISTANCE + ">";
    }
}
