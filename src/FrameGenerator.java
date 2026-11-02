
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FrameGenerator {

    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;

    public static List<BufferedImage> generate(
            Volume3D volume,
            List<PlaneInput> planes) {

        List<BufferedImage> frames = new ArrayList<>();

        for (int i = 0; i < planes.size(); i++) {

            PlaneInput input = planes.get(i);

            try {
                if (!input.isValid()) {
                    throw new IllegalArgumentException(
                            input.getError());
                }

                Point3D[] p = input.getPoints();
                long[][] pixels;

                if (p.length == 3) {
                    pixels = PlaneProjector.project(
                            volume, p[0], p[1], p[2]);

                } else if (p.length == 4) {
                    pixels = PlaneProjector.projectTrapezoid(
                            volume, p[0], p[1], p[2], p[3]);

                } else {
                    throw new IllegalArgumentException(
                            "Invalid number of points");
                }

                frames.add(createImage(pixels));

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Frame " + (i + 1)
                                + " ERROR: " + e.getMessage());

                frames.add(createErrorFrame(i + 1));
            }
        }

        return frames;
    }

    private static BufferedImage createImage(long[][] pixels) {

        int sourceHeight = pixels.length;
        int sourceWidth = pixels[0].length;

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        // Find minimum and maximum non-zero values.
        for (long[] row : pixels) {
            for (long value : row) {

                if (value == 0) {
                    continue;
                }

                if (value < min) {
                    min = value;
                }

                if (value > max) {
                    max = value;
                }
            }
        }

        if (min == Long.MAX_VALUE) {
            min = 0;
            max = 0;
        }

        BufferedImage frame = new BufferedImage(
                WIDTH,
                HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        double scale = Math.min(
                (double) WIDTH / sourceWidth,
                (double) HEIGHT / sourceHeight);

        int w = Math.max(
                1, (int) Math.round(sourceWidth * scale));

        int h = Math.max(
                1, (int) Math.round(sourceHeight * scale));

        int offsetX = (WIDTH - w) / 2;
        int offsetY = (HEIGHT - h) / 2;

        // Convert directly into the final video frame.
        for (int y = 0; y < h; y++) {

            int sourceY = Math.min(
                    sourceHeight - 1,
                    (int) ((y + 0.5) * sourceHeight / h));

            for (int x = 0; x < w; x++) {

                int sourceX = Math.min(
                        sourceWidth - 1,
                        (int) ((x + 0.5) * sourceWidth / w));

                long value = pixels[sourceY][sourceX];
                int gray;

                if (value == 0) {
                    gray = 0;

                } else if (max == min) {
                    gray = 128;

                } else {
                    gray = (int) Math.round(
                            255.0 * (value - min) / (max - min));
                }

                gray = Math.max(0, Math.min(255, gray));

                int rgb = (gray << 16)
                        | (gray << 8)
                        | gray;

                frame.setRGB(
                        offsetX + x,
                        offsetY + y,
                        rgb);
            }
        }

        return frame;
    }

    private static BufferedImage createErrorFrame(int number) {

        BufferedImage frame = new BufferedImage(
                WIDTH,
                HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = frame.createGraphics();

        try {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.RED);
            g.drawString(
                    "ERROR - Invalid plane", 75, 110);
            g.drawString(
                    "Frame: " + number, 75, 135);

        } finally {
            g.dispose();
        }

        return frame;
    }
}
