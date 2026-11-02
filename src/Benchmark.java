
public class Benchmark {

    private static final int WARMUP = 20;
    private static final int REPETITIONS = 100;

    public static void main(String[] args) {

        int size = 500;

        System.out.println("Creating synthetic 500x500x500 volume...");

        Volume3D volume = new Volume3D(size, size, size);

        for (int z = 0; z < size; z++) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    volume.setVoxel(x, y, z, x + y + z);
                }
            }
        }

        System.out.println("Volume created successfully!");
        System.out.println("ACTUAL VOLUME: "
                + volume.getSizeX() + " x "
                + volume.getSizeY() + " x "
                + volume.getSizeZ());

        Point3D a = new Point3D(250, 50, 250);
        Point3D b = new Point3D(50, 450, 250);
        Point3D c = new Point3D(450, 450, 250);

        long[][] sample = PlaneProjector.project(volume, a, b, c);

        System.out.println("PROJECTION SIZE: "
                + sample.length + " x " + sample[0].length);

        System.out.println("Warming up...");

        for (int i = 0; i < WARMUP; i++) {
            PlaneProjector.project(volume, a, b, c);
        }

        System.out.println("Starting benchmark...");

        long checksum = 0;

        long start = System.nanoTime();

        for (int i = 0; i < REPETITIONS; i++) {

            long[][] image = PlaneProjector.project(volume, a, b, c);

            checksum += image[image.length / 2][image[0].length / 2];
        }

        long end = System.nanoTime();

        double seconds = (end - start) / 1_000_000_000.0;
        double fps = REPETITIONS / seconds;

        System.out.println("Frames: " + REPETITIONS);
        System.out.printf("Projection time: %.4f seconds%n", seconds);
        System.out.printf("Projection FPS: %.2f%n", fps);
        System.out.println("Checksum: " + checksum);
    }
}
