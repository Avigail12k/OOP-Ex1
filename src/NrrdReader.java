
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class NrrdReader {

    public static Volume3D read(String filename)
            throws IOException {

        try (InputStream input = new BufferedInputStream(
                Files.newInputStream(Path.of(filename)))) {

            String magic = readLine(input);
            if (magic == null || !magic.matches("NRRD\\d{4}")) {
                throw new IOException("Invalid NRRD file");
            }

            Map<String, String> header = new HashMap<>();
            String line;

            while ((line = readLine(input)) != null
                    && !line.isEmpty()) {

                if (line.startsWith("#")) {
                    continue;
                }

                int colon = line.indexOf(':');
                if (colon >= 0) {
                    String key = line.substring(0, colon)
                            .trim().toLowerCase(Locale.ROOT);
                    String value = line.substring(colon + 1)
                            .trim().toLowerCase(Locale.ROOT);
                    header.put(key, value);
                }
            }

            if (line == null) {
                throw new IOException("Missing NRRD data");
            }

            if (!"3".equals(header.get("dimension"))) {
                throw new IOException("Expected 3D volume");
            }

            int bytesPerVoxel = getBytesPerVoxel(
                    header.get("type"));

            String encoding = header.get("encoding");
            if (!"raw".equals(encoding)
                    && !"ascii".equals(encoding)
                    && !"text".equals(encoding)
                    && !"txt".equals(encoding)) {
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

            int sx, sy, sz;

            try {
                sx = Integer.parseInt(parts[0]);
                sy = Integer.parseInt(parts[1]);
                sz = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid volume sizes", e);
            }

            Volume3D volume;
            try {
                volume = new Volume3D(sx, sy, sz);
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid volume dimensions", e);
            }

            boolean littleEndian = true;

            if ("raw".equals(encoding) && bytesPerVoxel > 1) {
                String endian = header.get("endian");

                if (!"little".equals(endian)
                        && !"big".equals(endian)) {
                    throw new IOException(
                            "Missing or invalid endian field");
                }

                littleEndian = "little".equals(endian);
            }

            Scanner scanner = null;
            if (!"raw".equals(encoding)) {
                scanner = new Scanner(
                        input, StandardCharsets.US_ASCII);
                scanner.useLocale(Locale.ROOT);
            }

            long maximum = (1L << (bytesPerVoxel * 8)) - 1;

            for (int z = 0; z < sz; z++) {
                for (int y = 0; y < sy; y++) {
                    for (int x = 0; x < sx; x++) {

                        long value;

                        if (scanner != null) {
                            if (!scanner.hasNextLong()) {
                                throw new IOException(
                                        "Invalid or incomplete ASCII data");
                            }

                            value = scanner.nextLong();
                        } else {
                            value = readUnsigned(
                                    input, bytesPerVoxel,
                                    littleEndian);
                        }

                        if (value < 0 || value > maximum) {
                            throw new IOException(
                                    "Voxel value out of range");
                        }

                        volume.setVoxel(x, y, z, value);
                    }
                }
            }

            return volume;
        }
    }

    private static int getBytesPerVoxel(String type)
            throws IOException {

        if (type == null) {
            throw new IOException("Missing data type");
        }

        return switch (type) {
            case "uint8", "uchar", "unsigned char" -> 1;
            case "uint16", "ushort", "unsigned short" -> 2;
            case "uint32", "uint", "unsigned int" -> 4;
            default -> throw new IOException(
                    "Unsupported data type: " + type);
        };
    }

    private static long readUnsigned(
            InputStream input, int count,
            boolean littleEndian) throws IOException {

        long value = 0;

        for (int i = 0; i < count; i++) {
            int b = input.read();

            if (b == -1) {
                throw new EOFException(
                        "Incomplete volume data");
            }

            int shift = littleEndian
                    ? 8 * i
                    : 8 * (count - 1 - i);

            value |= (long) b << shift;
        }

        return value;
    }

    private static String readLine(InputStream input)
            throws IOException {

        ByteArrayOutputStream buffer =
                new ByteArrayOutputStream();

        int b;
        while ((b = input.read()) != -1) {
            if (b == '\n') {
                break;
            }

            if (buffer.size() >= 65536) {
                throw new IOException("Header line too long");
            }

            buffer.write(b);
        }

        if (b == -1 && buffer.size() == 0) {
            return null;
        }

        return buffer.toString(StandardCharsets.US_ASCII)
                .replaceFirst("\\r$", "");
    }
}
