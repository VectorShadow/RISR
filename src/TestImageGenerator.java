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
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        double center = ((double)size - 1.0) / 2.0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                bi.setRGB(j, i, RING_COLORS[(int)RMath.distance(center, i, j) % RING_COLORS.length]);
            }
        }
        return bi;
    }
}