
# Software Requirements Specification (SRS)

## 1. Introduction

### 1.1 Purpose

This document specifies the functional and non-functional
requirements for the 3D Volume Plane Projection application.

The system is developed in Java 21 as part of Assignment 1
in the Object-Oriented Programming course at Ariel University.

### 1.2 Scope

The application reads a three-dimensional raster volume,
processes geometric regions defined by three or four points,
and produces an MP4 video containing the resulting 2D
projections.

The project emphasizes correctness, robustness,
object-oriented design, and computational efficiency.

### 1.3 Terminology

- Voxel: A single value in a three-dimensional volume.
- Projection: A two-dimensional image extracted from the volume.
- NRRD: A file format for multidimensional raster data.
- CSV: A file containing ordered geometric definitions.
- FPS: Frames processed per second.
- EPS: Geometric comparison tolerance, equal to 0.001.

## 2. Input Requirements

### FR-01: NRRD Volume Input

The system shall read three-dimensional volumes stored
in NRRD format.

Required voxel types:

- Unsigned 8-bit integers.
- Unsigned 16-bit integers.
- Unsigned 32-bit integers.

Each dimension shall satisfy:

`1 <= sizeX, sizeY, sizeZ < 1024`

The implementation supports raw, ASCII, and gzip
encodings, as well as appropriate byte-order handling
for multibyte binary values.

Signed 16-bit NRRD files are additionally supported
for the MRI demonstration.

Unsupported formats and invalid headers shall cause
a descriptive input exception.

### FR-02: CSV Plane Definitions

The application shall read an ordered sequence of
plane definitions from a CSV file.

Each record shall contain either:

- 9 coordinates defining three 3D points.
- 12 coordinates defining four 3D points.

Coordinates shall be finite numeric values.

The original order of the input records shall be
preserved during frame generation.

Malformed records shall be recorded as individual
input errors without preventing later records
from being processed.

### FR-03: Command-Line Arguments

The application shall accept three arguments:

1. Input NRRD filename.
2. Input CSV filename.
3. Output MP4 filename.

Example:

`data/test.nrrd data/planes.csv data/output.mp4`

The application shall report the expected usage when
the number of arguments is incorrect.

## 3. Geometric Requirements

### FR-04: Three-Point Geometry

For three points A, B, and C, the system shall validate
an isosceles triangle.

The following conditions shall hold:

- All points shall be defined and finite.
- The points shall not be collinear.
- The distances AB and AC shall be approximately equal.

The equality condition is:

`abs(dist(A,B) - dist(A,C)) < EPS`

where:

`EPS = 0.001`

Invalid definitions shall cause an
IllegalArgumentException.

### FR-05: Four-Point Geometry

For points A1, A2, B, and C, the system shall validate
an isosceles trapezoid.

The following conditions shall hold:

- The four points shall be coplanar.
- The two bases shall be parallel.
- The two legs shall be approximately equal.
- The upper base shall not exceed the lower base.
- The region shall not be degenerate.

The required distance conditions are:

`abs(dist(A1,B) - dist(A2,C)) < EPS`

`dist(A1,A2) <= dist(B,C)`

The implementation also checks base direction and
nondegeneracy to reject invalid configurations.

## 4. Projection Requirements

### FR-06: Triangle Projection

For a valid isosceles triangle, the application shall
generate a 2D image by sampling between the two sides
from A to B and from A to C.

The approximate output dimensions shall be derived
from the lengths AB and BC.

The implementation shall:

1. Determine the output image dimensions.
2. Advance along both triangle sides.
3. Sample between the corresponding side points.
4. Center each row in the output image.
5. Store the sampled voxel values.

### FR-07: Trapezoid Projection

For a valid isosceles trapezoid, the application shall
perform the corresponding row-by-row sampling process
between the sides A1-B and A2-C.

The approximate image dimensions shall be derived
from the geometric side lengths and the longer base.

### FR-08: Voxel Sampling

The system shall use nearest-neighbor sampling.

For each sampled 3D coordinate, the nearest integer
voxel coordinate shall be selected.

When the selected coordinate lies outside the input
volume, the sampled value shall be zero.

The projection algorithm shall not require
super-resolution or interpolation between voxel values.

## 5. Error Handling Requirements

### FR-09: Invalid Geometry

Invalid geometric definitions shall raise
IllegalArgumentException.

The demo application shall catch errors associated
with individual plane records.

### FR-10: Error Frames

For each invalid plane record, the application shall
generate a visually distinct error frame.

The frame shall occupy the same sequence position
as the corresponding CSV record.

Processing shall continue with subsequent records.

### FR-11: Invalid Volume Input

Malformed, incomplete, or unsupported NRRD input
shall cause a descriptive input exception.

The application is not required to generate a video
from an unreadable input volume.

## 6. Output Requirements

### FR-12: MP4 Video

The application shall create an MP4 video from the
generated frames.

The number and order of frames shall correspond
to the processed plane records.

All video frames shall have consistent dimensions
suitable for video encoding.

### FR-13: Visualization

Voxel intensities may be converted to grayscale
for visual representation.

The visualization transformation shall be separate
from the underlying voxel sampling algorithm.

Error frames shall remain visually distinguishable
from valid projection frames.

## 7. Non-Functional Requirements

### NFR-01: Correctness

The system shall implement the geometric validation
and projection behavior described in the assignment.

### NFR-02: Robustness

Invalid CSV records or plane definitions shall not
terminate processing of the entire CSV sequence.

### NFR-03: Performance

For a 500 x 500 x 500 volume containing byte values,
the assignment identifies approximately 100 frames
per second as the desired performance target.

A rate above 30 FPS is the mandatory minimum
specified by the assignment.

Performance shall be measured and documented.

Microbenchmarks of the projection algorithm shall
be distinguished from full image-rendering and
video-encoding performance.

### NFR-04: Maintainability

The implementation shall use modular object-oriented
classes with clear responsibilities.

Input parsing, geometric validation, projection,
frame generation, and video encoding shall be
separated into different components.

### NFR-05: Memory Efficiency

The system shall store voxel values efficiently
while preserving unsigned 8-, 16-, and 32-bit values.

### NFR-06: Reproducibility

The repository shall provide build instructions,
run instructions, test data references, and
documented experiment results.

## 8. Testing Requirements

### TR-01: Geometric Correctness

Tests shall cover:

- Valid isosceles triangles.
- Valid isosceles trapezoids.
- Degenerate geometric configurations.
- Non-coplanar points.
- EPS tolerance boundaries.
- Equal-length trapezoid bases.

### TR-02: Sampling Correctness

Tests shall cover:

- Expected image dimensions.
- Correct voxel selection.
- Nearest-neighbor sampling.
- Out-of-bounds sampling.
- Unsigned 32-bit value preservation.

### TR-03: Input Validation

Tests shall cover:

- Malformed CSV records.
- Unsupported NRRD types.
- Invalid dimensions.
- Incomplete input data.
- Supported binary byte orders.

### TR-04: Integration Testing

An integration test shall demonstrate:

1. Reading an NRRD file.
2. Reading ordered CSV records.
3. Generating valid projection frames.
4. Generating error frames.
5. Encoding an MP4 file.

### TR-05: Performance Testing

Performance experiments shall include representative
volume dimensions and multiple projection operations.

The report shall identify the measured workload,
number of repetitions, elapsed time, and processing rate.

## 9. Verification Status

The following development results have been recorded:

- A functional NRRD-to-MP4 demonstration.
- An MRI demonstration using a 256 x 256 x 130 volume.
- Successful handling of valid and invalid CSV records.
- 17 passing projection-related test assertions.
- A projection benchmark on a 256 x 256 x 130 MRI volume.
- A projection benchmark on a synthetic 300 x 300 x 300 volume.

The recorded benchmark rates were:

- MRI: 6107.23 projections per second.
- Synthetic volume: 5081.61 projections per second.

These benchmarks used repeated projections of the
same triangle and excluded video encoding.

They do not establish end-to-end compliance with
the 500 x 500 x 500 performance requirement.

## 10. Deliverables

The final repository shall include:

- Java source code.
- Maven build configuration.
- README with build and run instructions.
- Software Requirements Specification.
- Detailed projection algorithm description.
- Object-oriented design documentation.
- Testing strategy and experiment results.
- A runnable MP4 demonstration or documented
  access to the demonstration output.

The repository shall be made publicly accessible
for submission.

Submission shall also include the required
student identification and repository URL through
the course submission channels.
