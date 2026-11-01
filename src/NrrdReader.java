
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class NrrdReader
{
    public static Volume3D read(String filename)
            throws IOException
    {
        try (InputStream input = new BufferedInputStream(
                Files.newInputStream(Path.of(filename))))
        {
            Map<String, String> header = new HashMap<>();
            String magic = readLine(input);
            if (magic == null ||
                    !magic.matches("NRRD\\d{4}"))
            {
                throw new IOException("Invalid NRRD file");
            }
            String line;
            while ((line = readLine(input)) != null
                    && !line.isEmpty())
            {
                if (line.startsWith("#"))
                {
                    continue;
                }
                int colon = line.indexOf(':');
                if (colon < 0)
                {
                    continue;
                }

                header.put(
                        line.substring(0, colon).trim(),
                        line.substring(colon + 1).trim()
                );
            }

            if (line == null)
            {
                throw new IOException("Missing NRRD data");
            }

            if (!"3".equals(header.get("dimension")))
            {
                throw new IOException("Expected 3D volume");
            }

            String type = header.get("type");
            if (!"uint8".equals(type) &&
                    !"uchar".equals(type) &&
                    !"unsigned char".equals(type))
            {
                throw new IOException("Unsupported data type");
            }

            String encoding = header.get("encoding");

            if (!"raw".equals(encoding) && !"ascii".equals(encoding)) {
                throw new IOException("Unsupported encoding");
            }
            String sizes = header.get("sizes");
            if (sizes == null) {
                throw new IOException("Missing sizes");
            }

            String[] parts = sizes.split("\\s+");

            if (parts.length != 3) {
                throw new IOException("Invalid dimensions");
            }
            int sx = Integer.parseInt(parts[0]);
            int sy = Integer.parseInt(parts[1]);
            int sz = Integer.parseInt(parts[2]);
            Volume3D volume = new Volume3D(sx, sy, sz);

            java.util.Scanner scanner = null;

            if ("ascii".equals(encoding)) {
                scanner = new java.util.Scanner(
                        input, StandardCharsets.US_ASCII);
                scanner.useLocale(java.util.Locale.ROOT);
            }

            for (int z = 0; z < sz; z++) {
                for (int y = 0; y < sy; y++) {
                    for (int x = 0; x < sx; x++) {

                        int value;

                        if ("ascii".equals(encoding)) {
                            if (!scanner.hasNextInt()) {
                                throw new IOException(
                                        "Invalid or incomplete ASCII data");
                            }
                            value = scanner.nextInt();
                            if (value < 0 || value > 255) {
                                throw new IOException(
                                        "Voxel value out of range");
                            }
                        } else {
                            value = input.read();
                            if (value == -1) {
                                throw new EOFException(
                                        "Incomplete volume data");
                            }
                        }

                        volume.setVoxel(x, y, z, value);
                    }
                }
            }


            return volume;
        }
    }
    private static String readLine(InputStream input)
            throws IOException
    {
        ByteArrayOutputStream buffer =
                new ByteArrayOutputStream();
        int b;
        while ((b = input.read()) != -1)
        {
            if (b == '\n') {
                break;
            }
            if (buffer.size() >= 65536)
            {
                throw new IOException("Header line too long");
            }
            buffer.write(b);
        }
        if (b == -1 && buffer.size() == 0)
        {
            return null;
        }
        String result = buffer.toString(
                StandardCharsets.US_ASCII);
        return result.replaceFirst("\\r$", "");
    }
}
