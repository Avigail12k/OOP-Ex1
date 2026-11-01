
public class Main {

    public static void main(String[] args) {

        Volume3D volume = new Volume3D(6, 6, 6);

        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                for (int z = 0; z < 6; z++) {
                    volume.setVoxel(x, y, z, 100);
                }
            }
        }

        Point3D a1 = new Point3D(1, 1, 0);
        Point3D a2 = new Point3D(3, 1, 0);
        Point3D b = new Point3D(0, 4, 0);
        Point3D c = new Point3D(4, 4, 0);

        long[][] image = PlaneProjector.projectTrapezoid(
                volume, a1, a2, b, c);

        for (long[] row : image) {
            for (long pixel : row) {
                System.out.printf("%4d", pixel);
            }
            System.out.println();
        }

        try {
            ImageWriter.savePng(image, "data/trapezoid.png");
            System.out.println("Trapezoid image created!");
        } catch (java.io.IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
