
public class Main
{
    public static void main(String[] args)
    {
        Volume3D volume = new Volume3D(3, 3, 3);

        volume.setVoxel(1, 2, 0, 150);

        System.out.println(volume.getVoxel(1, 2, 0));
        System.out.println(volume.getSizeX());
        System.out.println(volume.getSizeY());
        System.out.println(volume.getSizeZ());

    }
}
