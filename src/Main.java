
public class Main
{
    public static void main(String[] args)
    {
        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(4, 0, 0);
        Point3D c = new Point3D(0, 3, 0);
        Plane3D plane = new Plane3D(a, b, c);
    }
}
