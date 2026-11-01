
public class Volume3D
{
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final int[][][] data;

    public Volume3D(int sizeX, int sizeY, int sizeZ)
    {
        if (sizeX < 1 || sizeY < 1 || sizeZ < 1 ||
                sizeX >= 1024 || sizeY >= 1024 || sizeZ >= 1024)
        {
            throw new IllegalArgumentException(
                    "Invalid volume dimensions");
        }
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        data = new int[sizeX][sizeY][sizeZ];
    }
    public void setVoxel(int x, int y, int z, int value)
    {
        data[x][y][z] = value;
    }

    public int getVoxel(int x, int y, int z)
    {
        return data[x][y][z];
    }

    public int getSizeX()
    {
        return sizeX;
    }

    public int getSizeY()
    {
        return sizeY;
    }

    public int getSizeZ()
    {
        return sizeZ;
    }
}

