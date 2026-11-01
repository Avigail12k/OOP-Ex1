
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<PlaneInput> read(String filename)
            throws IOException {

        List<PlaneInput> planes = new ArrayList<>();

        List<String> lines = Files.readAllLines(
                Path.of(filename));

        int lineNumber = 0;

        for (String line : lines) {
            lineNumber++;

            if (line.isBlank()) {
                continue;
            }

            try {
                String[] parts = line.split(",", -1);

                if (parts.length != 9 && parts.length != 12) {
                    throw new IllegalArgumentException(
                            "Expected 9 or 12 coordinates");
                }

                Point3D[] points =
                        new Point3D[parts.length / 3];

                for (int i = 0; i < points.length; i++) {
                    double x = Double.parseDouble(
                            parts[i * 3].trim());
                    double y = Double.parseDouble(
                            parts[i * 3 + 1].trim());
                    double z = Double.parseDouble(
                            parts[i * 3 + 2].trim());

                    if (!Double.isFinite(x) ||
                            !Double.isFinite(y) ||
                            !Double.isFinite(z)) {
                        throw new IllegalArgumentException(
                                "Non-finite coordinate");
                    }

                    points[i] = new Point3D(x, y, z);
                }

                planes.add(new PlaneInput(points, null));

            } catch (IllegalArgumentException e) {
                planes.add(new PlaneInput(
                        null,
                        "Line " + lineNumber + ": " +
                                e.getMessage()));
            }
        }

        return planes;
    }
}
