
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.ByteArrayOutputStream;

public class NrrdReaderTest
{
    public static void main(String[] args) throws Exception
    {
        check(1, true, new long[]{0, 150, 255});
        check(2, true, new long[]{0, 1000, 65535});
        check(2, false, new long[]{0, 1000, 65535});
        check(4, true, new long[]{0, 100000, 4294967295L});
        check(4, false, new long[]{0, 100000, 4294967295L});
        System.out.println("All binary NRRD tests passed!");
    }
    private static void check(
            int bytes, boolean little, long[] values)
            throws Exception
    {
        String type = switch (bytes)
        {
            case 1 -> "uint8";
            case 2 -> "uint16";
            case 4 -> "uint32";
            default -> throw new IllegalArgumentException();
        };
        String header = "NRRD0005\n" +
                "type: " + type + "\n" +
                "dimension: 3\n" +
                "sizes: 3 1 1\n" +
                "encoding: raw\n" +
                (bytes > 1 ? "endian: " +
                        (little ? "little" : "big") + "\n" : "") +
                "\n";
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();
        output.write(header.getBytes(StandardCharsets.US_ASCII));
        for (long value : values)
        {
            for (int i = 0; i < bytes; i++)
            {
                int shift = little
                        ? 8 * i
                        : 8 * (bytes - 1 - i);

                output.write((int) ((value >>> shift) & 255));
            }
        }
        Path file = Files.createTempFile(
                "nrrd-test-", ".nrrd");
        try
        {
            Files.write(file, output.toByteArray());
            Volume3D volume =
                    NrrdReader.read(file.toString());
            for (int x = 0; x < values.length; x++)
            {
                if (volume.getVoxel(x, 0, 0) != values[x])
                {
                    throw new AssertionError(
                            type + " failed at x=" + x);
                }
            }
            System.out.println(
                    "PASS: " + type +
                            (bytes == 1 ? "" :
                                    " " + (little ? "little" : "big")));
        } finally
        {
            Files.deleteIfExists(file);
        }
    }
}
