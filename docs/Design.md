
# Object-Oriented Design

## 1. Overview

The 3D Volume Plane Projection project is implemented
in Java 21 using a modular object-oriented design.

The system reads a 3D NRRD volume, processes geometric
regions defined in a CSV file, and generates an MP4 video
containing the resulting 2D projections.

The main design goal is separation of concerns.

Each class is responsible for a specific part of the
application, allowing components to be tested and
maintained independently.

## 2. System Architecture

The application is divided into five logical layers:

1. Data representation
2. Input parsing
3. Geometric validation
4. Projection and visualization
5. Video output and application coordination

### Data Flow

The main processing flow is:

    NRRD file
        |
        v
    NrrdReader
        |
        v
    Volume3D
        |
        +----------------------+
                               |
    CSV file                   |
        |                      |
        v                      |
    CsvReader                  |
        |                      |
        v                      |
    List<PlaneInput>           |
        |                      |
        +----------+-----------+
                   |
                   v
             FrameGenerator
                   |
                   v
             PlaneProjector
                   |
                   v
            Plane3D validation
                   |
                   v
             long[][] pixels
                   |
                   v
             BufferedImage
                   |
                   v
               VideoWriter
                   |
                   v
                MP4 file

Main coordinates the complete workflow.

The diagram is a conceptual view of the data flow.
In the implementation, FrameGenerator calls
PlaneProjector, which uses Volume3D and Plane3D.

## 3. Class Responsibilities

### 3.1 Point3D

Responsibility:
Represent an immutable point in three-dimensional space.

Fields:

- private final double x
- private final double y
- private final double z

Public operations:

- Point3D(double x, double y, double z)
- getX()
- getY()
- getZ()

The constructor rejects non-finite coordinates,
including NaN and Infinity.

The coordinates cannot be modified after construction.

### 3.2 Vector3D

Responsibility:
Represent a three-dimensional vector and provide
basic vector operations.

Fields:

- private final double x
- private final double y
- private final double z

Main operations:

- cross(Vector3D other)
- length()
- getX()
- getY()
- getZ()

The cross product is used to determine the normal
direction of a plane.

Vector length is used in geometric validation.

The class validates finite coordinates and rejects
null arguments in the cross-product operation.

### 3.3 Plane3D

Responsibility:
Validate geometric configurations used for projection.

Main operations:

- Plane3D(Point3D a, Point3D b, Point3D c)
- validateFourPoints(Point3D a1, Point3D a2,
  Point3D b, Point3D c)
- getPoint()
- getNormal()

For a three-point triangle, the class validates:

- Non-null points.
- Nondegenerate geometry.
- Approximately equal leg lengths.

For a four-point trapezoid, the class validates:

- Non-null points.
- Approximately equal leg lengths.
- Base-length restrictions.
- Parallel bases.
- Coplanarity.
- Nondegenerate geometry.
- Consistent base orientation.

The geometric tolerance is:

EPS = 0.001

Invalid geometric configurations cause
IllegalArgumentException.

The triangle constructor stores a reference point
and a normal vector.

### 3.4 Volume3D

Responsibility:
Store a three-dimensional voxel volume and provide
random access to individual voxel values.

Fields:

- private final int sizeX
- private final int sizeY
- private final int sizeZ
- private final int[] data

The volume is represented as a flattened array.

The index formula is:

index = (z * sizeY + y) * sizeX + x

Main operations:

- setVoxel(int x, int y, int z, long value)
- getVoxel(int x, int y, int z)
- getSizeX()
- getSizeY()
- getSizeZ()

The public interface uses long values to preserve
the full range of unsigned 32-bit integers.

Internally, the values are stored in an int array.

Unsigned reads use Integer.toUnsignedLong.

The class validates volume dimensions, voxel values,
and voxel coordinates.

### 3.5 NrrdReader

Responsibility:
Read NRRD input files and construct Volume3D objects.

Main operation:

- static Volume3D read(String filename)

The implementation parses NRRD header fields,
validates the volume dimensions and data type,
and reads voxel data.

Supported formats include:

- Unsigned 8-bit integers.
- Unsigned 16-bit integers.
- Unsigned 32-bit integers.
- Raw binary encoding.
- ASCII/text encoding.
- Gzip encoding.
- Little-endian and big-endian binary data.

Signed 16-bit data is additionally supported
for the MRI demonstration.

Invalid or unsupported input results in IOException.

### 3.6 CsvReader

Responsibility:
Parse ordered geometric definitions from a CSV file.

Main operation:

- static List<PlaneInput> read(String filename)

Each nonempty CSV record contains either 9 or
12 numeric coordinate values.

The reader parses each record independently.

Malformed records are represented by PlaneInput
objects containing an error description.

This prevents a single malformed record from
terminating processing of later records.

### 3.7 PlaneInput

Responsibility:
Represent the parsed result of one CSV record.

The object stores either:

- An array of 3 or 4 Point3D objects, or
- An error description.

Main operations:

- getPoints()
- getError()
- isValid()

The class provides a defensive copy of the point
array to protect its internal state.

The isValid method indicates parsing validity.
Geometric validity is checked separately.

### 3.8 PlaneProjector

Responsibility:
Generate two-dimensional projection arrays from
valid geometric definitions.

Main operations:

- project(Volume3D volume,
  Point3D a, Point3D b, Point3D c)

- projectTrapezoid(Volume3D volume,
  Point3D a1, Point3D a2,
  Point3D b, Point3D c)

The class validates geometry through Plane3D.

It then samples between corresponding points
on the two sides of the geometric region.

Nearest-neighbor voxel sampling is used.

The result is represented as long[][].

For sampled coordinates outside the volume,
the returned value is zero.

### 3.9 FrameGenerator

Responsibility:
Convert ordered geometric definitions into
fixed-size video frames.

Main operation:

- generate(Volume3D volume,
  List<PlaneInput> planes)

For each plane definition, the class:

1. Checks parsing validity.
2. Calls the appropriate projection method.
3. Converts projection values to grayscale.
4. Resizes the image while preserving aspect ratio.
5. Adds the frame to the output list.

Invalid geometric definitions are caught and
converted into visually distinct error frames.

The current video-frame dimensions are:

320 x 240 pixels

The implementation retains the ordered frames
in a List<BufferedImage> before video encoding.

### 3.10 VideoWriter

Responsibility:
Encode BufferedImage frames as an MP4 video.

Main operation:

- createMp4(List<BufferedImage> frames,
  String outputFile, int fps)

The implementation uses the JCodec library.

The class validates the frame list and frame rate,
then encodes the images in their original order.

### 3.11 ImageWriter

Responsibility:
Save two-dimensional projection arrays as PNG images.

This component is used for image output and
development testing.

It is separate from the MP4 encoding workflow.

### 3.12 Main

Responsibility:
Coordinate the application workflow.

Main performs the following operations:

1. Validate the number of command-line arguments.
2. Read the NRRD file.
3. Read the CSV plane definitions.
4. Generate ordered projection or error frames.
5. Create the output directory if necessary.
6. Encode the MP4 file.
7. Report execution results.

Main does not implement geometric calculations
or voxel sampling itself.

### 3.13 ProjectionTest

Responsibility:
Execute correctness checks for the geometry,
volume storage, and projection components.

The test class checks expected results and
reports passing or failing assertions.

The latest recorded run reported:

17 tests passed
0 tests failed

### 3.14 NrrdReaderTest

Responsibility:
Exercise NRRD parsing using controlled test data.

The test class includes cases for supported
integer widths and binary byte orders.

These tests are separate from the main program.

### 3.15 Benchmark

Responsibility:
Measure projection-processing performance.

The benchmark performs warm-up iterations
followed by repeated projection calculations.

The benchmark is separate from Main so that
performance experiments do not alter the
normal application workflow.

## 4. Main Dependencies

The important dependencies are:

- Main uses NrrdReader, CsvReader,
  FrameGenerator, and VideoWriter.
- NrrdReader creates Volume3D.
- CsvReader creates PlaneInput and Point3D.
- PlaneInput stores Point3D objects.
- Plane3D uses Point3D and Vector3D.
- PlaneProjector uses Volume3D, Point3D,
  and Plane3D.
- FrameGenerator uses PlaneInput,
  PlaneProjector, and BufferedImage.
- VideoWriter uses JCodec.
- ProjectionTest tests the geometry,
  volume, and projection components.
- Benchmark uses NrrdReader, Volume3D,
  Point3D, and PlaneProjector.

## 5. Object-Oriented Design Principles

### 5.1 Encapsulation

Fields are private, and data is accessed through
well-defined public methods.

Point3D and Vector3D are immutable.

Volume3D exposes controlled voxel access rather
than exposing its internal array.

### 5.2 Single Responsibility Principle

Different operations are separated into
different classes.

Examples:

- NrrdReader handles volume input.
- Plane3D handles geometric validation.
- PlaneProjector handles projection.
- FrameGenerator handles visualization.
- VideoWriter handles MP4 encoding.

### 5.3 Separation of Concerns

Input parsing, geometric validation, projection,
visualization, and file output are independent
parts of the implementation.

This separation supports debugging and testing.

### 5.4 Composition

The application uses composition and collaboration
between objects rather than a large inheritance
hierarchy.

For example, FrameGenerator collaborates with
PlaneProjector and uses PlaneInput objects.

### 5.5 Defensive Programming

The implementation validates:

- Non-finite coordinates.
- Invalid volume dimensions.
- Out-of-range voxel values.
- Degenerate geometric definitions.
- Invalid CSV records.
- Unsupported NRRD configurations.

Exceptions are used to report errors.

## 6. Error Handling Design

The application distinguishes between two
major error categories.

### 6.1 Input-Level Errors

Errors such as unreadable NRRD files or
unsupported NRRD data formats prevent the
application from constructing a valid volume.

These errors are reported as exceptions.

### 6.2 Plane-Level Errors

Invalid CSV records and invalid geometric
definitions are handled individually.

FrameGenerator catches the corresponding
IllegalArgumentException and creates an
error frame.

Processing then continues with later records.

This preserves the CSV record order.

## 7. Memory Design

Volume3D uses an int[] array.

Each voxel occupies four bytes in the array.

For example:

- A 256 x 256 x 130 volume requires
  approximately 34 MB of voxel-array storage.

- A 300 x 300 x 300 volume requires
  approximately 108 MB.

- A 500 x 500 x 500 volume requires
  approximately 500 MB.

These values exclude additional memory used
by Java objects, output images, and the runtime.

FrameGenerator currently stores all generated
BufferedImage frames before video encoding.

This increases memory usage as the number
of CSV records grows.

Streaming frames directly to VideoWriter could
reduce this memory requirement.

## 8. Performance Design

Volume3D provides O(1) voxel access.

The projection algorithm processes output
rows and samples without scanning the entire
input volume.

For output dimensions H x W:

Projection time complexity: O(H * W)

Projection output space complexity: O(H * W)

Volume storage space complexity:

O(sizeX * sizeY * sizeZ)

The actual end-to-end runtime also includes:

- File parsing.
- Frame generation.
- Grayscale conversion.
- Image resizing.
- Video encoding.

These operations must be considered separately
when evaluating total processing performance.

## 9. Design Tradeoffs

### Nearest-Neighbor Sampling

Advantages:

- Simple to implement.
- Constant-time voxel lookup.
- Permitted by the assignment.

Limitation:

- May produce visible pixelation.

### Fixed Video Resolution

Advantages:

- Consistent frame dimensions.
- Compatible with the video encoder.

Limitation:

- Large projection images may be scaled down.

### Full Frame List

Advantages:

- Simple encoding workflow.
- Straightforward preservation of frame order.

Limitation:

- Memory usage grows with the number of frames.

### Per-Frame Grayscale Normalization

Advantages:

- Improves visibility of MRI structures.
- Separates numerical voxel values from display values.

Limitations:

- Brightness may vary between frames.
- Zero-valued sampled voxels are not distinguished
  from unfilled projection pixels.

## 10. Testing and Verification

The implementation has been evaluated using:

- Synthetic volumes with known values.
- Invalid geometric configurations.
- Geometric tolerance tests.
- Out-of-bounds sampling tests.
- A real MRI dataset.
- Projection-only performance benchmarks.

Recorded projection benchmark results:

| Volume dimensions | Repetitions | Projection rate |
|---|---:|---:|
| 256 x 256 x 130 | 100 | 6107.23/s |
| 300 x 300 x 300 | 100 | 5081.61/s |

These results describe the tested workloads only.

They do not establish end-to-end throughput for
a 500 x 500 x 500 volume.

## 11. Known Limitations

- FrameGenerator stores all frames in memory
  before MP4 encoding.
- Rendering normalization differs per frame.
- Some temporary Point3D objects are allocated
  during projection.
- End-to-end FPS on a 500^3 volume has not
  been measured.
- The reader implements a supported subset
  of the full NRRD format.

## 12. Conclusion

The project uses modular object-oriented
components to separate volume representation,
input parsing, geometric validation, projection,
visualization, and MP4 generation.

This architecture supports independent testing,
clear responsibilities, and future improvements.

The implementation has passed the recorded
correctness tests and has successfully generated
MP4 demonstrations from both synthetic data
and a real MRI dataset.
