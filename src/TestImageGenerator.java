import java.awt.*;
import java.awt.image.BufferedImage;

public class TestImageGenerator {
    public static final int[] RING_COLORS = new int[]{
            Color.BLACK.getRGB(),
            Color.WHITE.getRGB(),
            Color.RED.getRGB(),
            Color.GREEN.getRGB(),
            Color.BLUE.getRGB(),
            Color.YELLOW.getRGB(),
            Color.MAGENTA.getRGB(),
            Color.CYAN.getRGB()
    };

    public static BufferedImage build(int size) {
        return build(size, size);
    }
    public static BufferedImage build(int rows, int columns) {
        BufferedImage bi = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_RGB);
        double centerRow = ((double)rows - 1.0) / 2.0;
        double centerCol = ((double)columns - 1.0) / 2.0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                bi.setRGB(j, i, RING_COLORS[(int)RMath.distance(centerRow, centerCol, i, j) % RING_COLORS.length]);
            }
        }
        return bi;
    }
}