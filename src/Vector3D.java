
public class Vector3D
{
    private final double x;
    private final double y;
    private final double z;

    public Vector3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public Vector3D cross(Vector3D other)
    {
        double nx = y * other.z - z * other.y;
        double ny = z * other.x - x * other.z;
        double nz = x * other.y - y * other.x;

        return new Vector3D(nx, ny, nz);
    }

    public double length()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }
}

