package dev;

import resources.RadialImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;



/**
 * Radial Image Scaling and Rotation
 *
 * by Vector Shadow Digital Labs
 */

public class Driver {
    public static void main(String[] args) throws IOException {
        final String PNG = "PNG";
        File sourceImage = new File("./images/sapphireTest.png");
        //ImageIO.write((new RadialImage(ImageIO.read(new File("./images/imageBorder.png")))).scale(0.2704).toBufferedImage(), PNG, new File("./images/imageBorder179.png"));
        ImageIO.write((new RadialImage(ImageIO.read(sourceImage))).scale(0.50).rotate(0.333).toBufferedImage(), PNG, new File("./images/halfsizeonethirdturn.png"));
        ImageIO.write((new RadialImage(ImageIO.read(sourceImage))).scale(2.0).rotate(0.667).toBufferedImage(), PNG, new File("./images/doublesizetwothirdsturn.png"));
        //ImageIO.write(TestImageGenerator.build(8_191), PNG, new File("./images/ring.png"));
    }
}
