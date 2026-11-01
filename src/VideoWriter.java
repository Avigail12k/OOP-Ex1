
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jcodec.api.awt.AWTSequenceEncoder;

public class VideoWriter {

    public static void createMp4(
            List<BufferedImage> frames,
            String outputFile,
            int fps) throws IOException {

        if (frames == null || frames.isEmpty()) {
            throw new IllegalArgumentException("No frames to encode");
        }

        if (fps <= 0) {
            throw new IllegalArgumentException("FPS must be positive");
        }

        AWTSequenceEncoder encoder =
                AWTSequenceEncoder.createSequenceEncoder(
                        new File(outputFile), fps);

        for (BufferedImage frame : frames) {
            encoder.encodeImage(frame);
        }

        encoder.finish();
    }
}
