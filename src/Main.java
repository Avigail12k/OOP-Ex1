
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            List<PlaneInput> planes =
                    CsvReader.read("data/planes.csv");

            System.out.println(
                    "Number of planes: " + planes.size());

            for (PlaneInput plane : planes) {
                if (!plane.isValid()) {
                    System.out.println(
                            "ERROR: " + plane.getError());
                    continue;
                }

                try {
                    Point3D[] p = plane.getPoints();

                    if (p.length == 3) {
                        new Plane3D(p[0], p[1], p[2]);
                    } else {
                        System.out.println(
                                "Four-point validation pending");
                        continue;
                    }

                    System.out.println("Valid plane");

                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "ERROR: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "File error: " + e.getMessage());
        }
    }
}
