
public class Plane3D {

    private static final double EPS = 0.001;

    private final Point3D a;
    private final Vector3D normal;

    public Plane3D(Point3D a, Point3D b, Point3D c) {

        if (a == null || b == null || c == null) {
            throw new IllegalArgumentException(
                    "Plane points cannot be null");
        }

        Vector3D ab = vector(a, b);
        Vector3D ac = vector(a, c);

        double lengthAB = ab.length();
        double lengthAC = ac.length();

        if (!Double.isFinite(lengthAB)
                || !Double.isFinite(lengthAC)
                || lengthAB == 0
                || lengthAC == 0) {
            throw new IllegalArgumentException(
                    "Invalid triangle sides");
        }

        if (Math.abs(lengthAB - lengthAC) >= EPS) {
            throw new IllegalArgumentException(
                    "The three points must form an isosceles triangle");
        }

        Vector3D n = ab.cross(ac);

        double relativeArea =
                n.length() / (lengthAB * lengthAC);

        if (!Double.isFinite(relativeArea)
                || relativeArea <= EPS) {
            throw new IllegalArgumentException(
                    "Points do not define a valid plane");
        }

        this.a = a;
        this.normal = n;
    }

    public Point3D getPoint() {
        return a;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public static void validateFourPoints(
            Point3D a1, Point3D a2,
            Point3D b, Point3D c) {

        if (a1 == null || a2 == null
                || b == null || c == null) {
            throw new IllegalArgumentException(
                    "Plane points cannot be null");
        }

        Vector3D base1 = vector(a1, a2);
        Vector3D base2 = vector(b, c);

        Vector3D side1 = vector(a1, b);
        Vector3D side2 = vector(a2, c);

        double upperBase = base1.length();
        double lowerBase = base2.length();

        double sideLength1 = side1.length();
        double sideLength2 = side2.length();

        if (!Double.isFinite(upperBase)
                || !Double.isFinite(lowerBase)
                || !Double.isFinite(sideLength1)
                || !Double.isFinite(sideLength2)
                || upperBase == 0
                || lowerBase == 0
                || sideLength1 == 0
                || sideLength2 == 0) {
            throw new IllegalArgumentException(
                    "Degenerate trapezoid");
        }

        if (Math.abs(sideLength1 - sideLength2) >= EPS) {
            throw new IllegalArgumentException(
                    "Trapezoid sides must have equal lengths");
        }

        if (upperBase > lowerBase) {
            throw new IllegalArgumentException(
                    "Upper base cannot exceed lower base");
        }

        // Check that the bases are parallel.
        double parallelError =
                base1.cross(base2).length()
                        / (upperBase * lowerBase);

        if (!Double.isFinite(parallelError)
                || parallelError > EPS) {
            throw new IllegalArgumentException(
                    "Trapezoid bases must be parallel");
        }

        // Check that the quadrilateral is not flat.
        Vector3D n = base1.cross(side1);

        double relativeArea =
                n.length() / (upperBase * sideLength1);

        if (!Double.isFinite(relativeArea)
                || relativeArea <= EPS) {
            throw new IllegalArgumentException(
                    "Degenerate trapezoid");
        }

        // Check that the fourth point lies in the same plane.
        Vector3D fourth = vector(a1, c);

        double planeDistance = Math.abs(
                n.getX() * fourth.getX()
                        + n.getY() * fourth.getY()
                        + n.getZ() * fourth.getZ()
        ) / n.length();

        if (!Double.isFinite(planeDistance)
                || planeDistance >= EPS) {
            throw new IllegalArgumentException(
                    "Points are not coplanar");
        }

        // Check that both bases point in the same direction.
        double dot =
                base1.getX() * base2.getX()
                        + base1.getY() * base2.getY()
                        + base1.getZ() * base2.getZ();

        if (dot <= 0) {
            throw new IllegalArgumentException(
                    "Trapezoid bases have inconsistent direction");
        }
    }

    private static Vector3D vector(Point3D from, Point3D to) {
        return new Vector3D(
                to.getX() - from.getX(),
                to.getY() - from.getY(),
                to.getZ() - from.getZ()
        );
    }
}
