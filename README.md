# RISR
Radial Image Scaling and Rotation

RISR provides methods for image manipulation.

The primary agent for this is a class called RadialImage, which represents pixels in terms of distance and direction, rather than row and column.
As a result, rotation methods work best the closer the source image is to a perfect square, and are better used on images which do not contain much data in the corners - any portion of the resulting image which is not fully contained in complete circles expanding outward from the center of the source image tends to warp, since the polar translation does not attempt to infer data not found in the source image.

Constructors:

Derive a RadialImage from a BufferedImage and infer the background color:
public RadialImage(BufferedImage bufferedImage);
Derive a RadialImage from a BufferedImage and supply the background color:
public RadialImage(BufferedImage bufferedImage, int backgroundColorRGB)

Currently implemented methods:
Derive a RadialImage by applying a rotation to an existing RadialImage 
(@param percentOfCircle the percentage of one full rotation to apply to this image):
public RadialImage rotate(double percentOfCircle)

Derive a BufferedImage from this RadialImage:
public BufferedImage toBufferedImage()

Unimplemented:
scale(double scaleFactor)
