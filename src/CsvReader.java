
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvReader
{

    public static List<Point3D[]> read(String filename)
            throws IOException {

        List<Point3D[]> planes = new ArrayList<>();

        List<String> lines = Files.readAllLines(
                Path.of(filename));

        for (String line : lines) {

            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split(",", -1);

            if (parts.length != 9 && parts.length != 12) {
                throw new IOException(
                        "Each row must contain 9 or 12 values");
            }

            Point3D[] points = new Point3D[parts.length / 3];

            try {
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
                        throw new NumberFormatException();
                    }

                    points[i] = new Point3D(x, y, z);
                }
            } catch (NumberFormatException e) {
                throw new IOException(
                        "Invalid coordinates in CSV", e);
            }

            planes.add(points);
        }

        return planes;
    }
}
