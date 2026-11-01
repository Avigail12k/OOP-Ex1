
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class ImageWriter
{
    public static void savePng(
            long[][] pixels,
            String filename) throws IOException
    {
        if (pixels == null || pixels.length == 0 ||
                pixels[0] == null || pixels[0].length == 0) {
            throw new IllegalArgumentException("Empty image");
        }
        int height = pixels.length;
        int width = pixels[0].length;

        BufferedImage image = new BufferedImage(
                width, height, BufferedImage.TYPE_BYTE_GRAY);

        long max = 0;

        for (long[] row : pixels) {
            if (row == null || row.length != width) {
                throw new IllegalArgumentException(
                        "Image rows must have equal lengths");
            }

            for (long value : row)
            {
                if (value < 0) {
                    throw new IllegalArgumentException(
                            "Negative pixel value");
                }
                max = Math.max(max, value);
            }
        }
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int gray = max == 0 ? 0 :
                        (int) Math.round(
                                255.0 * pixels[y][x] / max);

                int rgb = (gray << 16) | (gray << 8) | gray;

                image.setRGB(x, y, rgb);
            }
        }
        if (!ImageIO.write(image, "png", new File(filename))) {
            throw new IOException("PNG writer not available");
        }
    }
}
