
public class Plane3D
{
    private static final double EPS = 0.001;
    private final Point3D a;
    private final Vector3D normal;

    public Plane3D(Point3D a, Point3D b, Point3D c)
    {
        if (a == null || b == null || c == null)
        {
            throw new IllegalArgumentException(
                    "Plane points cannot be null");
        }
        Vector3D ab = new Vector3D(
                b.getX() - a.getX(),
                b.getY() - a.getY(),
                b.getZ() - a.getZ()
        );
        Vector3D ac = new Vector3D(
                c.getX() - a.getX(),
                c.getY() - a.getY(),
                c.getZ() - a.getZ()
        );
        Vector3D n = ab.cross(ac);
        double scale = ab.length() * ac.length();

        if (scale == 0 || n.length() / scale <= EPS)
        {
            throw new IllegalArgumentException(
                    "Points do not define a valid plane");
        }
        this.a = a;
        this.normal = n;
    }

    public Point3D getPoint()
    {
        return a;
    }

    public Vector3D getNormal()
    {
        return normal;
    }
}

