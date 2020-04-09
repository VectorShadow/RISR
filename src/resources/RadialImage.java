package resources;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Define an Image by concentric rings of pixels radiating out from a well-defined center.
 */
public class RadialImage {

    private static final double SORT_SCALE = (double)0x3fff;

    private final BufferedImage SOURCE;
    private final ArrayList<ArrayList<RadialPixel>> PIXEL_RINGS;
    private final int BACKGROUND_RGB;
    private final int ROWS;
    private final int COLS;

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
        SOURCE = bufferedImage;
        PIXEL_RINGS = new ArrayList<>();
        ROWS = SOURCE.getHeight();
        COLS = SOURCE.getWidth();
        //count the occurrence of each individual color - used to infer background
        HashMap<Integer, Integer> colorCounter = new HashMap<>();
        //find the index of the center pixel, either (n, n) for odd SIZE or (n.5, n.5) for even SIZE
        int distance;
        int rgb;
        for (int i = 0; i < SOURCE.getHeight(); ++i) {
            for (int j = 0; j < SOURCE.getWidth(); ++j) {
                GridCoordinate gc = new GridCoordinate(i, j);
                PolarCoordinate pc = RMath.convert(RMath.center(ROWS), RMath.center(COLS), gc);
                distance = (int)pc.DISTANCE;
                //ensure we have enough concentric pixel rings to track this pixel
                while (PIXEL_RINGS.size() <= distance)
                    PIXEL_RINGS.add(new ArrayList<>());
                //get the rgb value of the pixel in the source image
                rgb = SOURCE.getRGB(j, i);
                //track the count of pixels of this color
                if (colorCounter.containsKey(rgb))
                    colorCounter.put(rgb, colorCounter.get(rgb) + 1);
                else
                    colorCounter.put(rgb, 1);
                //add the pixel to the end of the ring at this distance from the center
                PIXEL_RINGS.get(distance).add(new RadialPixel(pc, rgb));
            }
        }
        //if we need to infer a background color, do so
        if (backgroundColorRGB == 0) {
            int mostCommonColor = 0;
            int mostCommonColorCount = -1;
            for (Map.Entry<Integer, Integer> e : colorCounter.entrySet()) {
                if (e.getValue() > mostCommonColorCount) {
                    mostCommonColor = e.getKey();
                    mostCommonColorCount = e.getValue();
                }
            }
            BACKGROUND_RGB = mostCommonColor;
        } else
            BACKGROUND_RGB = backgroundColorRGB;
        //todo - deal with outer unconnected rings?
        for (ArrayList<RadialPixel> ring : PIXEL_RINGS) {
            ring.sort(
                    (RadialPixel rp1, RadialPixel rp2) -> (int)(SORT_SCALE * (rp1.PC.ANGLE - rp2.PC.ANGLE))
            );
        }
    }

    public BufferedImage toBufferedImage() {
        return SOURCE;
    }

    /**
     * Derive a RadialImage by applying a rotation to an existing RadialImage.
     * @param percentOfCircle the percentage of one full rotation to apply to this image
     */
    public RadialImage rotate(double percentOfCircle) {
        final ArrayList<ArrayList<RadialPixel>> ROTATED_RINGS = new ArrayList<>();
        //normalize the rotation percentage
        double nRotate = RMath.normalize(percentOfCircle);
        int rOffset;
        for (ArrayList<RadialPixel> ring : PIXEL_RINGS) {
            //calculate the resulting offset for each ring
            rOffset = (int)(nRotate * (double)(ring.size()));
            if (rOffset == 0)
                ROTATED_RINGS.add(ring);
            else {
                //apply the resulting offset to construct a new ring.
                ArrayList<RadialPixel> rRing = new ArrayList<>();
                for (int i = rOffset; i < ring.size(); ++i)
                    rRing.add(new RadialPixel(ring.get(i - rOffset).PC, ring.get(i).RGB));
                for (int i = 0; i < rOffset; ++i)
                    rRing.add(new RadialPixel(ring.get((ring.size() - rOffset + i)).PC, ring.get(i).RGB));
                ROTATED_RINGS.add(rRing);
            }
        }
        return new RadialImage(generateBufferedImage(ROWS, COLS, ROTATED_RINGS));
    }
    /**
     * Derive a RadialImage by applying a scale factor to an existing RadialImage.
     * @param scaleFactor the ratio between the size of the new image and the size of the old image.
     */
    public RadialImage scale(double scaleFactor) {
        //set the new dimensions
        final int R = RMath.map(ROWS, scaleFactor);
        final int C = RMath.map(COLS, scaleFactor);
        //generate a radial image with rings corresponding to our desired outputs
        BufferedImage scaledImage = new BufferedImage(C, R, BufferedImage.TYPE_INT_RGB);
        RadialImage radialImage = new RadialImage(scaledImage);
        final ArrayList<ArrayList<RadialPixel>> SCALED_RINGS = new ArrayList<>();
        ArrayList<RadialPixel> ring;
        RadialPixel radialPixel;
        //establish the scale between the numbers of rings in each image
        double ringScale = RMath.getScale(PIXEL_RINGS.size(), radialImage.PIXEL_RINGS.size());
        double pixelScale;
        int sourceRingSize;
        int targetRingSize;
        //iterate through all rings in the source image
        for (int i = 0; i < PIXEL_RINGS.size(); ++i) {
            //ignore or repeat rings as called for by the scale
            while (SCALED_RINGS.size() < RMath.map(i + 1, ringScale)) {
                ring = new ArrayList<>();
                sourceRingSize = PIXEL_RINGS.get(i).size();
                targetRingSize = radialImage.PIXEL_RINGS.get(SCALED_RINGS.size()).size();
                //establish the scale between the numbers of pixels in this ring for each image
                pixelScale = RMath.getScale(sourceRingSize, targetRingSize);
                //iterate through all pixels in the source ring
                for (int j = 0; j < PIXEL_RINGS.get(i).size(); ++j) {
                    //ignore or repeat pixels as called for by the scale
                    while (ring.size() < RMath.map(j + 1, pixelScale)) {
                        //grab the target pixel from the dummy image to get an accurate angle and distance
                        radialPixel = radialImage.PIXEL_RINGS.get(SCALED_RINGS.size()).get(ring.size());
                        ring.add(
                                new RadialPixel(
                                        new PolarCoordinate(
                                                radialPixel.PC.ANGLE,
                                                radialPixel.PC.DISTANCE
                                        ),
                                        PIXEL_RINGS.get(i).get(j).RGB
                                )
                        );
                    }
                }
                SCALED_RINGS.add(ring);
            }
        }
        return new RadialImage(generateBufferedImage(R, C, SCALED_RINGS));
    }
    /**
     * Derive a BufferedImage from this RadialImage.
     */
    private BufferedImage generateBufferedImage(int imageRows, int imageColumns, ArrayList<ArrayList<RadialPixel>> rings) {
        BufferedImage bufferedImage = new BufferedImage(imageColumns, imageRows, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int j = 0; j < bufferedImage.getWidth(); ++j) {
                bufferedImage.setRGB(j, i, BACKGROUND_RGB);
            }
        }
        for (ArrayList<RadialPixel> ring : rings) {
            for (RadialPixel radialPixel : ring) {
                GridCoordinate gc = RMath.convert(RMath.center(imageRows), RMath.center(imageColumns), radialPixel.PC);
                bufferedImage.setRGB(gc.COL, gc.ROW, radialPixel.RGB);
            }
        }
        return bufferedImage;
    }
}
