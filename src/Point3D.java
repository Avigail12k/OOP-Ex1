
public class Point3D {

    private final double x;
    private final double y;
    private final double z;

    public Point3D(double x, double y, double z) {

        if (!Double.isFinite(x)
                || !Double.isFinite(y)
                || !Double.isFinite(z)) {

            throw new IllegalArgumentException(
                    "Point coordinates must be finite");
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
