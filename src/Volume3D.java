
public class Volume3D {

    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;

    private final int[] data;

    public Volume3D(int sizeX, int sizeY, int sizeZ) {

        if (sizeX < 1 || sizeY < 1 || sizeZ < 1 ||
                sizeX >= 1024 || sizeY >= 1024 || sizeZ >= 1024) {

            throw new IllegalArgumentException(
                    "Invalid volume dimensions");
        }

        long total = (long) sizeX * sizeY * sizeZ;

        if (total > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Volume is too large");
        }

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        data = new int[(int) total];
    }

    private int index(int x, int y, int z) {

        if (x < 0 || x >= sizeX ||
                y < 0 || y >= sizeY ||
                z < 0 || z >= sizeZ) {

            throw new IndexOutOfBoundsException(
                    "Voxel coordinates out of bounds");
        }

        return (z * sizeY + y) * sizeX + x;
    }

    public void setVoxel(int x, int y, int z, long value) {

        if (value < 0 || value > 0xFFFFFFFFL) {
            throw new IllegalArgumentException(
                    "Voxel value out of range");
        }

        data[index(x, y, z)] = (int) value;
    }

    public long getVoxel(int x, int y, int z) {

        return Integer.toUnsignedLong(data[index(x, y, z)]);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeZ() {
        return sizeZ;
    }
}
