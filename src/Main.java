
public class Main
{
    public static void main(String[] args)
    {
        try {
            Volume3D volume = NrrdReader.read("data/test.nrrd");

            System.out.println(volume.getVoxel(0, 0, 0));
            System.out.println(volume.getVoxel(1, 0, 0));
            System.out.println(volume.getVoxel(1, 1, 1));
        } catch (java.io.IOException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }
}
