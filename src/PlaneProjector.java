
public class PlaneProjector {

    public static long[][] project(
            Volume3D volume,
            Point3D a,
            Point3D b,
            Point3D c) {

        if (volume == null) {
            throw new IllegalArgumentException(
                    "Volume cannot be null");
        }

        // Validate the triangle
        new Plane3D(a, b, c);

        double dab = distance(a, b);
        double dbc = distance(b, c);

        int height = Math.max(1, (int) Math.ceil(dab) + 1);
        int width = Math.max(1, (int) Math.ceil(dbc) + 1);

        long[][] image = new long[height][width];

        // Move from vertex a towards side bc
        for (int y = 0; y < height; y++) {

            double t = (double) y / (height - 1);

            Point3D a1 = interpolate(a, b, t);
            Point3D a2 = interpolate(a, c, t);

            double segmentLength = distance(a1, a2);

            int samples = (int) Math.ceil(segmentLength) + 1;

            int startX = (width - samples) / 2;

            for (int x = 0; x < samples; x++) {

                double s = samples == 1
                        ? 0
                        : (double) x / (samples - 1);

                Point3D p = interpolate(a1, a2, s);

                long value = sampleNearest(volume, p);

                int imageX = startX + x;

                if (imageX >= 0 && imageX < width) {
                    image[y][imageX] = value;
                }
            }
        }

        return image;
    }

    private static Point3D interpolate(
            Point3D a, Point3D b, double t) {

        return new Point3D(
                a.getX() + t * (b.getX() - a.getX()),
                a.getY() + t * (b.getY() - a.getY()),
                a.getZ() + t * (b.getZ() - a.getZ())
        );
    }

    private static double distance(Point3D a, Point3D b)
    {

        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dz = a.getZ() - b.getZ();

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static long sampleNearest(
            Volume3D volume, Point3D p)
    {

        int x = (int) Math.round(p.getX());
        int y = (int) Math.round(p.getY());
        int z = (int) Math.round(p.getZ());

        if (x < 0 || x >= volume.getSizeX() ||
                y < 0 || y >= volume.getSizeY() ||
                z < 0 || z >= volume.getSizeZ()) {
            return 0;
        }

        return volume.getVoxel(x, y, z);
    }

    public static long[][] projectTrapezoid(
            Volume3D volume,
            Point3D a1, Point3D a2,
            Point3D b, Point3D c) {

        if (volume == null) {
            throw new IllegalArgumentException(
                    "Volume cannot be null");
        }

        Plane3D.validateFourPoints(a1, a2, b, c);

        double sideLength = distance(a1, b);
        double upperWidth = distance(a1, a2);
        double lowerWidth = distance(b, c);

        int height = Math.max(2,
                (int) Math.ceil(sideLength) + 1);

        int width = Math.max(1,
                (int) Math.ceil(lowerWidth) + 1);

        long[][] image = new long[height][width];

        for (int y = 0; y < height; y++) {

            double t = (double) y / (height - 1);

            Point3D left = interpolate(a1, b, t);
            Point3D right = interpolate(a2, c, t);

            double rowLength = distance(left, right);
            int samples = (int) Math.ceil(rowLength) + 1;

            int startX = (width - samples) / 2;

            for (int x = 0; x < samples; x++) {

                double s = samples == 1
                        ? 0
                        : (double) x / (samples - 1);

                Point3D p = interpolate(left, right, s);

                int imageX = startX + x;

                if (imageX >= 0 && imageX < width) {
                    image[y][imageX] =
                            sampleNearest(volume, p);
                }
            }
        }

        return image;
    }

}
