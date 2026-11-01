
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) throws Exception {

        Volume3D volume = new Volume3D(6, 6, 6);

        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                for (int z = 0; z < 6; z++) {
                    volume.setVoxel(x, y, z, 100);
                }
            }
        }

        List<PlaneInput> planes =
                CsvReader.read("data/planes.csv");

        List<BufferedImage> frames =
                FrameGenerator.generate(volume, planes);

        Files.createDirectories(Path.of("data/frames"));

        for (int i = 0; i < frames.size(); i++) {
            String filename = String.format(
                    "data/frames/frame_%03d.png", i + 1);

            ImageIO.write(frames.get(i), "png",
                    Path.of(filename).toFile());
        }

        System.out.println(
                "Created " + frames.size() + " frames");
        VideoWriter.createMp4(
                frames,
                "data/output.mp4",
                2
        );

        System.out.println("MP4 video created!");

    }
}
