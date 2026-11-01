
public class Main
{
    public static void main(String[] args)
    {
        Volume3D volume = new Volume3D(5, 5, 5);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 5; z++) {
                    volume.setVoxel(x, y, z, 100);
                }
            }
        }

        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(4, 0, 0);
        Point3D c = new Point3D(0, 4, 0);

        long[][] image = PlaneProjector.project(volume, a, b, c);

        for (long[] row : image) {
            for (long pixel : row) {
                System.out.printf("%4d", pixel);
            }
            System.out.println();
        }


    }
}
