
public class Main {

    public static void main(String[] args) {

        Point3D a1 = new Point3D(0, 0, 0);
        Point3D a2 = new Point3D(2, 0, 0);
        Point3D b = new Point3D(-1, 3, 0);
        Point3D c = new Point3D(3, 3, 0);

        try {
            Plane3D.validateFourPoints(a1, a2, b, c);
            System.out.println("Valid trapezoid");

        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
