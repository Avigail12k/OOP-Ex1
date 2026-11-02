
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.out.println(
                    "Usage: java Main <file.nrrd> <file.csv> <file.mp4>");
            return;
        }

        String nrrdFile = args[0];
        String csvFile = args[1];
        String mp4File = args[2];

        System.out.println("Reading NRRD...");
        Volume3D volume = NrrdReader.read(nrrdFile);

        System.out.println("Reading CSV...");
        List<PlaneInput> planes = CsvReader.read(csvFile);

        System.out.println("Generating frames...");

        long startTime = System.nanoTime();

        List<BufferedImage> frames =
                FrameGenerator.generate(volume, planes);

        long endTime = System.nanoTime();

        double seconds = (endTime - startTime) / 1_000_000_000.0;
        double fps = frames.size() / seconds;

        System.out.printf("Projection time: %.4f seconds%n", seconds);
        System.out.printf("Processing FPS: %.2f%n", fps);

        Path outputPath = Path.of(mp4File);
        Path parent = outputPath.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        System.out.println("Creating MP4...");
        VideoWriter.createMp4(frames, mp4File, 2);

        System.out.println(
                "Created " + frames.size() + " frames");

        System.out.println(
                "MP4 video created: " + mp4File);
    }
}
