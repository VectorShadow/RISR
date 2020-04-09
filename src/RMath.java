/**
 * Provide supporting functions for Radial Image transformations.
 */
class RMath {
    private static final double MAX_RADIANS = Math.PI * 2.0;
    /**
     * Find the angle from the center pixel of an image to a given pixel elsewhere in the image.
     */
    static double angle(double centerIndex, double pixelRow, double pixelCol) {
        double a = (centerIndex - pixelRow);
        double b = (centerIndex - pixelCol);
        return Math.atan2(a, b);
    }
    /**
     * Find the distance between the center pixel of an image and a given pixel elsewhere in the image.
     */
    static double distance(double centerIndex, double pixelRow, double pixelCol) {
        double a = (centerIndex - pixelRow);
        double b = (centerIndex - pixelCol);
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
    static PolarCoordinate convert(double centerPoint, GridCoordinate gc) {
        return new PolarCoordinate(
                angle(centerPoint, gc.ROW, gc.COL),
                distance(centerPoint, gc.ROW, gc.COL)
        );
    }
    /**
     * Convert a polar coordinate to a grid coordinate.
     */
    static GridCoordinate convert(double centerPoint, PolarCoordinate pc) {
        int invert = (int)(2 * centerPoint);
        return new GridCoordinate(
                invert - (int)Math.round(centerPoint + (Math.sin(pc.ANGLE) * pc.DISTANCE)),
                invert - (int)Math.round(centerPoint + (Math.cos(pc.ANGLE) * pc.DISTANCE))
        );
    }
}
