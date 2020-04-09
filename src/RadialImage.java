import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Define an Image by concentric rings of pixels radiating out from a well-defined center.
 */
public class RadialImage {
    private ArrayList<ArrayList<RadialPixel>> pixelRings;
    private final int BACKGROUND_RGB;
    private final int HEIGHT;
    private final int WIDTH;
    private final double VERTICAL_CENTER;
    private final double HORIZONTAL_CENTER;

    /**
     * Derive a RadialImage from a BufferedImage and infer the background color.
     */
    public RadialImage(BufferedImage bufferedImage) {
        this(bufferedImage, 0);
    }

    /**
     * Derive a RadialImage from a BufferedImage and supply the background color.
     */
    public RadialImage(BufferedImage bufferedImage, int backgroundColorRGB) {
        pixelRings = new ArrayList<>();
        HEIGHT = bufferedImage.getHeight();
        WIDTH = bufferedImage.getWidth();
        //count the occurrence of each individual color - used to infer background
        HashMap<Integer, Integer> colorCounter = new HashMap<>();
        //find the index of the center pixel, either (n, n) for odd SIZE or (n.5, n.5) for even SIZE
        VERTICAL_CENTER = ((double)(HEIGHT - 1) / 2.0);
        HORIZONTAL_CENTER = ((double)(WIDTH - 1) / 2.0);
        int distance;
        int rgb;
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int j = 0; j < bufferedImage.getWidth(); ++j) {
                GridCoordinate gc = new GridCoordinate(i, j);
                PolarCoordinate pc = RMath.convert(VERTICAL_CENTER, HORIZONTAL_CENTER, gc);
                distance = (int)pc.DISTANCE;
                //ensure we have enough concentric pixel rings to track this pixel
                while (pixelRings.size() <= distance)
                    pixelRings.add(new ArrayList<>());
                //get the rgb value of the pixel in the source image
                rgb = bufferedImage.getRGB(j, i);
                //track the count of pixels of this color
                if (colorCounter.containsKey(rgb))
                    colorCounter.put(rgb, colorCounter.get(rgb) + 1);
                else
                    colorCounter.put(rgb, 1);
                //add the pixel to the end of the ring at this distance from the center
                pixelRings.get(distance).add(new RadialPixel(pc, rgb));
            }
        }
        //if we need to infer a background color, do so
        if (backgroundColorRGB == 0) {
            int mostCommonColor = 0;
            int mostCommonColorCount = -1;
            for (Map.Entry<Integer, Integer> e : colorCounter.entrySet()) {
                if (e.getValue() > mostCommonColorCount)
                    mostCommonColor = e.getKey();
            }
            BACKGROUND_RGB = mostCommonColor;
        } else
            BACKGROUND_RGB = backgroundColorRGB;
        //todo - deal with outer unconnected rings?
        for (ArrayList<RadialPixel> ring : pixelRings) {
            ring.sort(
                    (RadialPixel rp1, RadialPixel rp2) -> (int)(100.0 * (rp1.PC.ANGLE - rp2.PC.ANGLE))
            );
        }
    }

    /**
     * Copy the paramaters of an existing RadialImage.
     * @param radialImage
     */
    private RadialImage(RadialImage radialImage) {
        pixelRings = new ArrayList<>();
        BACKGROUND_RGB = radialImage.BACKGROUND_RGB;
        HEIGHT = radialImage.HEIGHT;
        WIDTH = radialImage.WIDTH;
        VERTICAL_CENTER = radialImage.VERTICAL_CENTER;
        HORIZONTAL_CENTER = radialImage.HORIZONTAL_CENTER;
    }

    /**
     * Derive a RadialImage by applying a rotation to an existing RadialImage.
     * @param percentOfCircle the percentage of one full rotation to apply to this image
     */
    public RadialImage rotate(double percentOfCircle) {
        //copy this image
        RadialImage rotatedImage = new RadialImage(this);
        //normalize the rotation percentage
        double nRotate = RMath.normalize(percentOfCircle);
        int rOffset;
        for (ArrayList<RadialPixel> ring : pixelRings) {
            //calculate the resulting offset for each ring
            rOffset = (int)(nRotate * (double)(ring.size()));
            if (rOffset == 0)
                rotatedImage.pixelRings.add(ring);
            else {
                //apply the resulting offset to construct a new ring.
                ArrayList<RadialPixel> rRing = new ArrayList<>();
                for (int i = rOffset; i < ring.size(); ++i)
                    rRing.add(new RadialPixel(ring.get(i - rOffset).PC, ring.get(i).RGB));
                for (int i = 0; i < rOffset; ++i)
                    rRing.add(new RadialPixel(ring.get((ring.size() - rOffset + i)).PC, ring.get(i).RGB));
                rotatedImage.pixelRings.add(rRing);
            }
        }
        return rotatedImage;
    }
    public RadialImage scale(double scaleFactor) {
        throw new UnsupportedOperationException(); //todo
    }

    /**
     * Derive a BufferedImage from this RadialImage.
     */
    public BufferedImage toBufferedImage() {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int j = 0; j < bufferedImage.getWidth(); ++j) {
                bufferedImage.setRGB(j, i, BACKGROUND_RGB);
            }
        }
        for (ArrayList<RadialPixel> ring : pixelRings) {
            for (RadialPixel radialPixel : ring) {
                GridCoordinate gc = RMath.convert(VERTICAL_CENTER, HORIZONTAL_CENTER, radialPixel.PC);
                if (inBounds(gc))
                    bufferedImage.setRGB(gc.COL, gc.ROW, radialPixel.RGB);
            }
        }
        return bufferedImage;
    }

    private boolean inBounds(GridCoordinate gc) {
        return gc.ROW >= 0 && gc.COL >= 0 && gc.ROW < HEIGHT && gc.COL < WIDTH;
    }
}
