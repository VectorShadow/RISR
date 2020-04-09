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
     * Convert a double corresponding to any amount of rotation into a single turn forward rotation.
     */
    static double normalize(double d) {
        int i = (int)d;
        return d == i ? 0.0 : d > 0 ? d - i : 1.0 + (d - i);
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
        int invertRow = (int)(2 * rowCenter);
        int invertCol = (int)(2 * colCenter);
        return new GridCoordinate(
                invertRow - (int)Math.round(rowCenter + (Math.sin(pc.ANGLE) * pc.DISTANCE)),
                invertCol - (int)Math.round(colCenter + (Math.cos(pc.ANGLE) * pc.DISTANCE))
        );
    }
}
