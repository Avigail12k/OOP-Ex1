
public class Main
{
    public static void main(String[] args)
    {

        Vector3D u = new Vector3D(1, 0, 0);
        Vector3D v = new Vector3D(0, 1, 0);

        Vector3D result = u.cross(v);

        System.out.println(result.getX());
        System.out.println(result.getY());
        System.out.println(result.getZ());

        System.out.println("Length: " + result.length());

    }
}
