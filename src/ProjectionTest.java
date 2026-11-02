
public class ProjectionTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        testTriangle();
        testTrapezoid();
        testOutOfBounds();
        testInvalidTriangle();
        testInvalidTrapezoid();

        System.out.println();
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Some tests failed");
        }
    }

    private static void check(
            String name, boolean condition) {

        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static void testTriangle() {

        Volume3D volume = new Volume3D(5, 5, 5);

        for (int z = 0; z < 5; z++) {
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    volume.setVoxel(x, y, z, 100);
                }
            }
        }

        long[][] image = PlaneProjector.project(
                volume,
                new Point3D(0, 0, 2),
                new Point3D(4, 0, 2),
                new Point3D(0, 4, 2));

        check("Triangle dimensions",
                image.length == 5
                        && image[0].length == 7);

        check("Triangle sampled value",
                image[4][3] == 100);

        check("Triangle outside area",
                image[0][0] == 0);
    }

    private static void testTrapezoid() {

        Volume3D volume = new Volume3D(6, 6, 2);

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                volume.setVoxel(x, y, 0, 150);
            }
        }

        long[][] image = PlaneProjector.projectTrapezoid(
                volume,
                new Point3D(2, 1, 0),
                new Point3D(3, 1, 0),
                new Point3D(1, 4, 0),
                new Point3D(4, 4, 0));

        check("Trapezoid dimensions",
                image.length == 5
                        && image[0].length == 4);

        check("Trapezoid sampled value",
                image[4][1] == 150);
    }

    private static void testOutOfBounds() {

        Volume3D volume = new Volume3D(3, 3, 3);

        long[][] image = PlaneProjector.project(
                volume,
                new Point3D(10, 10, 1),
                new Point3D(12, 10, 1),
                new Point3D(10, 12, 1));

        boolean allZero = true;

        for (long[] row : image) {
            for (long value : row) {
                if (value != 0) {
                    allZero = false;
                }
            }
        }

        check("Out-of-bounds samples are zero",
                allZero);
    }

    private static void testInvalidTriangle() {

        boolean rejected = false;

        try {
            PlaneProjector.project(
                    new Volume3D(5, 5, 5),
                    new Point3D(0, 0, 0),
                    new Point3D(2, 0, 0),
                    new Point3D(4, 0, 0));

        } catch (IllegalArgumentException e) {
            rejected = true;
        }

        check("Invalid triangle rejected", rejected);
    }

    private static void testInvalidTrapezoid() {

        boolean rejected = false;

        try {
            PlaneProjector.projectTrapezoid(
                    new Volume3D(6, 6, 6),
                    new Point3D(2, 1, 0),
                    new Point3D(3, 1, 0),
                    new Point3D(1, 4, 0),
                    new Point3D(4, 4, 1));

        } catch (IllegalArgumentException e) {
            rejected = true;
        }

        check("Invalid trapezoid rejected", rejected);
    }
}

