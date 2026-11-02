
# Experiment and Testing Report

## 1. Purpose

This document describes the testing strategy,
correctness verification, and performance experiments
performed for the 3D Volume Plane Projection project.

The main goals are to evaluate:

- Correctness of geometric validation.
- Correctness of nearest-neighbor voxel sampling.
- Robustness when processing invalid plane definitions.
- Integration of NRRD, CSV, and MP4 components.
- Runtime performance of the projection algorithm.

## 2. Testing Environment

Programming language: Java 21

Development environment: IntelliJ IDEA

Build system: Maven

Video encoding library: JCodec 0.2.5

Operating system: macOS

Tests were executed on the development computer.

Hardware specifications were not recorded.

## 3. Test Datasets

### 3.1 Synthetic Test Volumes

Small synthetic volumes were used to verify
the correctness of the projection algorithm.

These volumes contain known voxel values,
allowing expected results to be calculated
and compared with actual output.

Examples include:

- Constant voxel intensities.
- Coordinate-dependent voxel intensities.
- Out-of-bounds projection regions.
- Maximum unsigned 32-bit voxel values.

### 3.2 MRI Dataset

A real MRI volume was used for integration testing.

Dataset: MRHead_1.nrrd

Dimensions: 256 x 256 x 130

Data type: signed 16-bit integer

Encoding: gzip

Byte order: little-endian

The MRI file was used to verify compatibility
with compressed real-world NRRD data.

Signed 16-bit support is an extension beyond the
assignment's required unsigned integer types.

### 3.3 Large Synthetic Volume

An additional synthetic volume was created
with dimensions:

300 x 300 x 300

Voxel values were generated using:

value = x + y + z

This dataset was used for a projection-only
performance experiment.

## 4. Correctness Testing

The ProjectionTest class was used to test
geometry, voxel access, and projection behavior.

The tests covered:

- Triangle output dimensions.
- Triangle sampled voxel values.
- Pixels outside triangular regions.
- Trapezoid output dimensions.
- Trapezoid sampled voxel values.
- Out-of-bounds sampling.
- Invalid triangle rejection.
- Invalid trapezoid rejection.
- Geometric tolerance EPS = 0.001.
- Acceptance of valid equal-length trapezoid bases.
- Rejection of NaN and Infinity coordinates.
- Nearest-neighbor sampling.
- Preservation of unsigned 32-bit voxel values.

### Recorded Test Result

Tests passed: 17

Tests failed: 0

The test program terminated successfully.

These results demonstrate correctness for the
implemented test cases, but they do not constitute
a formal proof for every possible input.

## 5. NRRD Reader Testing

The NrrdReaderTest class contains tests for
controlled NRRD input data.

The implemented reader supports unsigned 8-bit,
16-bit, and 32-bit values, raw and text data,
gzip compression, and binary byte-order handling.

The MRI demonstration also exercised gzip
decoding and signed 16-bit data handling.

A complete independent test report for every
reader configuration has not yet been recorded.

## 6. Integration Testing

The complete application was executed using
three input/output arguments:

data/MRHead_1.nrrd
data/mri_planes.csv
data/mri_demo.mp4

The CSV file contained:

- Three valid isosceles triangles.
- One valid isosceles trapezoid.

### Integration Result

The program successfully:

1. Loaded the MRI volume.
2. Parsed the CSV records.
3. Generated four projection frames.
4. Encoded the frames into an MP4 file.
5. Completed with exit code 0.

The resulting video showed anatomical MRI
structures inside the triangular and
trapezoidal sampled regions.

A separate synthetic CSV test included two
invalid definitions, producing error frames
without stopping processing of later records.

## 7. Performance Methodology

The Benchmark class measures the time required
to calculate repeated projections.

The benchmark uses:

- 20 warm-up repetitions.
- 100 measured repetitions.

Time is measured using System.nanoTime().

The processing rate is calculated as:

FPS = number_of_projections / elapsed_seconds

The benchmark excludes:

- NRRD file loading.
- CSV file reading.
- Grayscale image conversion.
- Video frame resizing.
- MP4 encoding.

Therefore, the results represent projection-only
throughput, rather than end-to-end video processing.

## 8. Performance Results

### Experiment A: MRI Volume

Volume dimensions:

256 x 256 x 130

Measured projections: 100

Elapsed time: 0.0164 seconds

Projection rate: 6107.23 projections/second

### Experiment B: Synthetic Volume

Volume dimensions:

300 x 300 x 300

Measured projections: 100

Elapsed time: 0.0197 seconds

Projection rate: 5081.61 projections/second

### Results Summary

| Experiment | Volume dimensions | Projections | Time | Projection rate |
|---|---|---:|---:|---:|
| MRI | 256 x 256 x 130 | 100 | 0.0164 s | 6107.23/s |
| Synthetic | 300 x 300 x 300 | 100 | 0.0197 s | 5081.61/s |

## 9. Discussion

The measured projection-only throughput is
substantially higher than 30 projections per
second for both tested workloads.

However, the same triangular region was
projected repeatedly during each experiment.

The results may benefit from JVM warm-up,
cache locality, and repeated access to similar
memory locations.

These measurements do not demonstrate that
the complete video-generation pipeline
achieves the same rate.

In addition, the synthetic experiment used
a 300 x 300 x 300 volume rather than
the 500 x 500 x 500 volume mentioned
in the assignment.

Therefore, the mandatory performance target
has not yet been conclusively verified
for the complete specified workload.

## 10. Memory Considerations

Volume3D stores voxel values in an int array.

Each voxel occupies four bytes.

Approximate voxel-array memory requirements:

| Volume dimensions | Memory |
|---|---:|
| 256 x 256 x 130 | 34 MB |
| 300 x 300 x 300 | 108 MB |
| 500 x 500 x 500 | 500 MB |

These numbers exclude other memory allocations.

The current FrameGenerator stores all output
frames in memory before video encoding.

This design is simple but may be less suitable
for very large CSV input files.

## 11. Limitations

The current experiment has the following limitations:

- The 500 x 500 x 500 workload was not measured.
- Performance tests reused the same projection.
- End-to-end video encoding throughput was not measured.
- Detailed hardware specifications were not recorded.
- Grayscale normalization may vary across frames.
- A full independent NRRD reader test report
  is not yet available.

These limitations should be considered when
interpreting the results.

## 12. Conclusion

The implementation successfully completed
the recorded correctness tests and generated
an MP4 demonstration from a real MRI volume.

The synthetic and MRI projection benchmarks
showed high projection-only processing rates.

Additional measurements are required to
confirm end-to-end performance against the
assignment's 500 x 500 x 500 workload.

The experiments support the functionality
of the implemented projection system for
the tested cases.
