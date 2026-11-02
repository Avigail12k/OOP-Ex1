
public class Benchmark {

    private static final int WARMUP = 20;
    private static final int REPETITIONS = 100;

    public static void main(String[] args) throws Exception {

        System.out.println("Loading MRI...");

        Volume3D volume = new Volume3D(300, 300, 300);

        for (int z = 0; z < 300; z++) {
            for (int y = 0; y < 300; y++) {
                for (int x = 0; x < 300; x++) {
                    volume.setVoxel(x, y, z, x + y + z);
                }
            }
        }

        Point3D a = new Point3D(128, 40, 65);
        Point3D b = new Point3D(40, 215, 65);
        Point3D c = new Point3D(216, 215, 65);

        // Warm up the JVM before measuring.
        for (int i = 0; i < WARMUP; i++) {
            PlaneProjector.project(volume, a, b, c);
        }

        System.out.println("Starting benchmark...");

        long start = System.nanoTime();

        long checksum = 0;

        for (int i = 0; i < REPETITIONS; i++) {

            long[][] image =
                    PlaneProjector.project(volume, a, b, c);

            checksum += image[image.length / 2]
                    [image[0].length / 2];
        }

        long end = System.nanoTime();

        double seconds =
                (end - start) / 1_000_000_000.0;

        double fps = REPETITIONS / seconds;

        System.out.println("Frames: " + REPETITIONS);

        System.out.printf(
                "Projection time: %.4f seconds%n",
                seconds);

        System.out.printf(
                "Projection FPS: %.2f%n",
                fps);

        System.out.println("Checksum: " + checksum);
    }
}
