package dev;

import java.awt.*;
import java.awt.image.BufferedImage;

import static resources.RMath.*;

public class TestImageGenerator {

    public static BufferedImage build(int size) {
        return build(size, size);
    }
    public static BufferedImage build(int rows, int columns) {
        BufferedImage bi = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_RGB);
        double centerRow = ((double)rows - 1.0) / 2.0;
        double centerCol = ((double)columns - 1.0) / 2.0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                bi.setRGB(j, i, primeRings((int) distance(centerRow, centerCol, i, j)).getRGB());
            }
        }
        return bi;
    }
    private static Color primeRings(int distance) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (distance == 0) return Color.BLACK;
        if (distance == 1) return Color.WHITE;
        if (distance % 2 == 0) r += 128;
        if (distance % 3 == 0) g += 128;
        if (distance % 5 == 0) b += 128;
        if (distance % 7 == 0) r += 64;
        if (distance % 11 == 0) g += 64;
        if (distance % 13 == 0) b += 64;
        if (distance % 17 == 0) r += 32;
        if (distance % 19 == 0) g += 32;
        if (distance % 23 == 0) b += 32;
        if (distance % 29 == 0) r += 16;
        if (distance % 31 == 0) g += 16;
        if (distance % 37 == 0) b += 16;
        if (distance % 41 == 0) r += 8;
        if (distance % 43 == 0) g += 8;
        if (distance % 47 == 0) b += 8;
        if (distance % 53 == 0) r += 4;
        if (distance % 59 == 0) g += 4;
        if (distance % 61 == 0) b += 4;
        if (distance % 67 == 0) r += 2;
        if (distance % 71 == 0) g += 2;
        if (distance % 73 == 0) b += 2;
        if (distance % 79 == 0) r += 1;
        if (distance % 83 == 0) g += 1;
        if (distance % 89 == 0) b += 1;
        return new Color(r, g, b);
    }
}