
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

        double distanceAB = ab.length();
        double distanceAC = ac.length();

        if (Math.abs(distanceAB - distanceAC) >= EPS) {
            throw new IllegalArgumentException(
                    "The three points must form an isosceles triangle");
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


    public static void validateFourPoints(
            Point3D a1, Point3D a2,
            Point3D b, Point3D c) {

        if (a1 == null || a2 == null ||
                b == null || c == null) {
            throw new IllegalArgumentException(
                    "Plane points cannot be null");
        }

        Vector3D side1 = new Vector3D(
                b.getX() - a1.getX(),
                b.getY() - a1.getY(),
                b.getZ() - a1.getZ()
        );

        Vector3D side2 = new Vector3D(
                c.getX() - a2.getX(),
                c.getY() - a2.getY(),
                c.getZ() - a2.getZ()
        );

        double firstBase = distance(a1, a2);
        double secondBase = distance(b, c);

        if (Math.abs(side1.length() - side2.length()) >= EPS) {
            throw new IllegalArgumentException(
                    "Trapezoid sides must have equal lengths");
        }

        if (firstBase <= EPS || secondBase <= EPS ||
                firstBase > secondBase) {
            throw new IllegalArgumentException(
                    "Invalid trapezoid bases");
        }

        Vector3D base1 = new Vector3D(
                a2.getX() - a1.getX(),
                a2.getY() - a1.getY(),
                a2.getZ() - a1.getZ()
        );

        Vector3D base2 = new Vector3D(
                c.getX() - b.getX(),
                c.getY() - b.getY(),
                c.getZ() - b.getZ()
        );

        double parallelError =
                base1.cross(base2).length() /
                        (base1.length() * base2.length());

        if (parallelError > EPS) {
            throw new IllegalArgumentException(
                    "Trapezoid bases must be parallel");
        }

        Vector3D normal = base1.cross(side1);

        if (normal.length() <= EPS) {
            throw new IllegalArgumentException(
                    "Degenerate trapezoid");
        }

        Vector3D fourth = new Vector3D(
                c.getX() - a1.getX(),
                c.getY() - a1.getY(),
                c.getZ() - a1.getZ()
        );

        double planeDistance = Math.abs(
                normal.getX() * fourth.getX() +
                        normal.getY() * fourth.getY() +
                        normal.getZ() * fourth.getZ()
        ) / normal.length();

        if (planeDistance >= EPS) {
            throw new IllegalArgumentException(
                    "Points are not coplanar");
        }
    }

    private static double distance(Point3D a, Point3D b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dz = a.getZ() - b.getZ();

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

}

