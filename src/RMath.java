/**
 * Provide supporting functions for Radial Image transformations.
 */
class RMath {
    private static final double MAX_RADIANS = Math.PI * 2.0;
    /**
     * Find the angle from the center pixel of an image to a given pixel elsewhere in the image.
     */
    static double angle(double rowCenter, double colCenter, double pixelRow, double pixelCol) {
        double a = (rowCenter - pixelRow);
        double b = (colCenter - pixelCol);
        return Math.atan2(a, b);
    }
    /**
     * Find the center pixel of an image with the provided dimension.
     */
    static double center(int dimension) {
        return ((double)dimension - 1.0) / 2.0;
    }
    /**
     * Convert a grid coordinate to a polar coordinate.
     */
    static PolarCoordinate convert(double rowCenter, double colCenter, GridCoordinate gc) {
        return new PolarCoordinate(
                angle(rowCenter, colCenter, gc.ROW, gc.COL),
                distance(rowCenter, colCenter, gc.ROW, gc.COL)
        );
    }
    /**
     * Convert a polar coordinate to a grid coordinate.
     */
    static GridCoordinate convert(double rowCenter, double colCenter, PolarCoordinate pc) {
        return new GridCoordinate(
                (int)Math.round(rowCenter - (Math.sin(pc.ANGLE) * pc.DISTANCE)),
                (int)Math.round(colCenter - (Math.cos(pc.ANGLE) * pc.DISTANCE))
        );
    }
    /**
     * Find the distance between the center pixel of an image and a given pixel elsewhere in the image.
     */
    static double distance(double rowCenter, double colCenter, double pixelRow, double pixelCol) {
        double a = (rowCenter - pixelRow);
        double b = (colCenter - pixelCol);
        double a2 = a * a;
        double b2 = b * b;
        double c2 = a2 + b2;
        return Math.sqrt(c2);
    }

    /**
     * Find the scale between two integers.
     */
    static double getScale(int source, int target) {
        return (double)target / (double)source;
    }
    /**
     * Map an index in 1.0 scale to an index in the provided scale.
     */
    static int map(int source, double scale) {
        return (int)Math.round((double)source * scale);
    }
    /**
     * Convert a double corresponding to any amount of rotation into a single turn forward rotation.
     */
    static double normalize(double d) {
        int i = (int)d;
        return d == i ? 0.0 : d > 0 ? d - i : 1.0 + (d - i);
    }
}
