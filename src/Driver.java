import javax.imageio.ImageIO;
import java.awt.*;
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
        ImageIO.write(TestImageGenerator.build(101), PNG, new File("./images/test3.png"));
        File sourceImage = new File("./images/test4.png");
        File simpleOutput = new File("./images/out0.png");
        File rotate33Output = new File("./images/r33.png");
        File rotate45Output = new File("./images/r45.png");
        File rotate77Output = new File("./images/r77.png");
        File rotate133Output = new File("./images/r133.png");
        File rotate50Output = new File("./images/r50.png");
        File rotateminus33Output = new File("./images/r-33.png");
        BufferedImage testImage = ImageIO.read(sourceImage);
        for (int i = 0; i < testImage.getHeight(); ++i){
            for (int j = 0; j < testImage.getWidth(); ++j) {
                GridCoordinate in = new GridCoordinate(i, j);
                PolarCoordinate pole = RMath.convert((testImage.getHeight() - 1) / 2, in);
                GridCoordinate out = RMath.convert((testImage.getHeight() - 1) / 2, pole);
                if (in.COL != out.COL || in.ROW != out.ROW)
                    throw new ArithmeticException("Out column was " + out.COL + ", expected " + in.COL + ". Out row was " + out.ROW + ", expected " + in.ROW + ".");
            }
        }
        RadialImage radialImage = new RadialImage(testImage, Color.WHITE.getRGB());
        BufferedImage simpleTest = radialImage.toBufferedImage();
        ImageIO.write(simpleTest, "PNG", simpleOutput);
        RadialImage rotate33 = radialImage.rotate(.33);
        RadialImage rotate45 = radialImage.rotate(.45);
        RadialImage rotate77 = radialImage.rotate(.77);
        RadialImage rotate133 = radialImage.rotate(1.33);
        RadialImage rotate50 = radialImage.rotate(.5);
        RadialImage rotateminus33 = radialImage.rotate(-0.33);
        ImageIO.write(rotate33.toBufferedImage(), "PNG", rotate33Output);
        ImageIO.write(rotate45.toBufferedImage(), "PNG", rotate45Output);
        ImageIO.write(rotate77.toBufferedImage(), "PNG", rotate77Output);
        ImageIO.write(rotate133.toBufferedImage(), "PNG", rotate133Output);
        ImageIO.write(rotate50.toBufferedImage(), "PNG", rotate50Output);
        ImageIO.write(rotateminus33.toBufferedImage(), "PNG", rotateminus33Output);
    }
}
