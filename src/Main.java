
import java.util.List;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            List<Point3D[]> planes =
                    CsvReader.read("data/planes.csv");

            System.out.println(
                    "Number of planes: " + planes.size());

            for (Point3D[] points : planes) {
                System.out.println(
                        "Points in plane: " + points.length);
            }

        } catch (IOException e) {
            System.out.println(
                    "Error: " + e.getMessage());
        }
    }
}
