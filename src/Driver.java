import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
        File rotate20Output = new File("./images/rotateOut20.png");
        File scaleOutput66 = new File("./images/scaleOut66.png");
        File scaleOutput150 = new File("./images/scaleOut150.png");
        File twinOutput = new File("./images/twinOut.png");
        ImageIO.write(TestImageGenerator.build(662), PNG, new File("./images/ring.png"));
        BufferedImage testImage = ImageIO.read(sourceImage);
        RadialImage radialImage = new RadialImage(testImage);
        ImageIO.write(radialImage.rotate(0.2).toBufferedImage(), PNG, rotate20Output);
        ImageIO.write(radialImage.scale(0.66).toBufferedImage(), PNG, scaleOutput66);
        ImageIO.write(radialImage.scale(1.5).toBufferedImage(), PNG, scaleOutput150);
        ImageIO.write(radialImage.rotate(.2).scale(0.85).toBufferedImage(), PNG, twinOutput);
    }
}
