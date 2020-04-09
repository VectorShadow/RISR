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
        File scaleOutput45 = new File("./images/scaleOut45.png");
        File scaleOutput350 = new File("./images/scaleOut350.png");
        File twinOutput = new File("./images/twinOut.png");
        //ImageIO.write(TestImageGenerator.build(8_191), PNG, new File("./images/ring.png"));
        BufferedImage testImage = ImageIO.read(sourceImage);
        RadialImage radialImage = new RadialImage(testImage);
        ImageIO.write(radialImage.rotate(0.2).toBufferedImage(), PNG, rotate20Output);
        ImageIO.write(radialImage.scale(0.45).toBufferedImage(), PNG, scaleOutput45);
        ImageIO.write(radialImage.scale(3.5).toBufferedImage(), PNG, scaleOutput350);
        ImageIO.write(radialImage.rotate(.25).scale(0.85).toBufferedImage(), PNG, twinOutput);
    }
}
