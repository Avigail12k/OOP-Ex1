
public class Vector3D {

    private final double x;
    private final double y;
    private final double z;

    public Vector3D(double x, double y, double z) {

        if (!Double.isFinite(x)
                || !Double.isFinite(y)
                || !Double.isFinite(z)) {

            throw new IllegalArgumentException(
                    "Vector coordinates must be finite");
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

    public Vector3D cross(Vector3D other) {

        if (other == null) {
            throw new IllegalArgumentException(
                    "Other vector cannot be null");
        }

        double nx = y * other.z - z * other.y;
        double ny = z * other.x - x * other.z;
        double nz = x * other.y - y * other.x;

        return new Vector3D(nx, ny, nz);
    }

    public double length() {
        return Math.hypot(Math.hypot(x, y), z);
    }
}
