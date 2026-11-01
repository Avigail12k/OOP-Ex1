
public class Main
{
    public static void main(String[] args)
    {

        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(4, 0, 0);
        Point3D c = new Point3D(0, 4, 0);

        try {
            Plane3D plane = new Plane3D(a, b, c);
            System.out.println("Valid plane");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }



    }
}
