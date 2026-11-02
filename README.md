
# 3D Volume Plane Projection

## Project Overview

This project was developed in Java 21 for the Object-Oriented Programming course at Ariel University.

The application extracts 2D images from a 3D volume using geometric regions defined by three or four points. The resulting frames are encoded into an MP4 video in the same order as the CSV input records.

The main goals are correctness, object-oriented design, robustness, error handling, and runtime efficiency.

## Problem Definition

The application receives three command-line arguments:

1. An input NRRD file containing a 3D volume.
2. An input CSV file containing plane definitions.
3. An output MP4 file path.

Each CSV record contains either:
- 9 coordinates representing three 3D points.
- 12 coordinates representing four 3D points.

Three points define an isosceles triangle. Four points define an isosceles trapezoid.

For each valid definition, the program samples voxels from the 3D volume and creates a 2D frame.

Invalid plane definitions produce visually distinct error frames without terminating the entire sequence.

## Supported NRRD Formats

The reader supports:

- Three-dimensional volumes.
- Unsigned 8-bit integers.
- Unsigned 16-bit integers.
- Unsigned 32-bit integers.
- Raw binary encoding.
- ASCII/text encoding.
- Gzip-compressed encoding.
- Little-endian and big-endian binary data.

Signed 16-bit NRRD data is additionally supported for the MRI demonstration. Signed values are shifted into a non-negative range for storage.

Volume dimensions must satisfy:

`1 <= sizeX, sizeY, sizeZ < 1024`

## Geometric Validation

### Isosceles Triangle

For points A, B, and C:

- The points must define a nondegenerate plane.
- The lengths AB and AC must be approximately equal.
- The geometric tolerance is EPS = 0.001.

### Isosceles Trapezoid

For points A1, A2, B, and C:

- All points must be coplanar.
- The two bases must be parallel.
- The two legs must have approximately equal lengths.
- The upper base must not exceed the lower base.
- Degenerate configurations are rejected.

## Projection Algorithm

The projection algorithm uses nearest-neighbor sampling.

For each valid region:

1. Validate the geometric definition.
2. Calculate approximate output image dimensions.
3. Interpolate along the two sides.
4. Sample points across each row.
5. Read the nearest voxel from the 3D volume.
6. Return zero for locations outside the volume.
7. Center the sampled rows in the output image.

## Object-Oriented Design

The application uses separate classes for different responsibilities.

| Class | Responsibility |
|---|---|
| Point3D | Immutable 3D point |
| Vector3D | Vector calculations |
| Plane3D | Geometric validation |
| Volume3D | 3D voxel storage |
| NrrdReader | NRRD file parsing |
| CsvReader | CSV plane parsing |
| PlaneInput | Plane record or parsing error |
| PlaneProjector | Triangle and trapezoid projections |
| FrameGenerator | Video frame generation |
| VideoWriter | MP4 video encoding |
| ImageWriter | PNG image output |
| Main | Application entry point |
| Benchmark | Projection performance measurement |
| ProjectionTest | Projection correctness tests |
| NrrdReaderTest | NRRD reader tests |

## Technologies

- Java 21
- Maven
- IntelliJ IDEA
- JCodec (MP4 encoding)

## Building the Project

Requirements:

- JDK 21
- Maven

Clone the repository and open the project directory.

Compile the project:

```bash
mvn compile
```

Maven downloads the required dependencies.

## Running the Application

The application requires three arguments:

```text
<input.nrrd> <planes.csv> <output.mp4>
```

Run the project using Maven:

```bash
mvn compile exec:java -Dexec.mainClass=Main -Dexec.args="data/test.nrrd data/planes.csv data/output.mp4"
```

Alternatively, open the project in IntelliJ IDEA and configure:

- Main class: Main
- Working directory: project root
- Program arguments: input NRRD, input CSV, output MP4

Example:

```text
data/test.nrrd data/planes.csv data/output.mp4
```

## MRI Demonstration

The application was tested with the MRHead_1.nrrd MRI dataset.

Dataset properties:

- Dimensions: 256 x 256 x 130
- Data type: signed 16-bit integer
- Encoding: gzip
- Endian: little

The demonstration CSV file is:

`data/mri_planes.csv`

It contains three triangular definitions and one trapezoidal definition.

The demonstration was run with:

```text
data/MRHead_1.nrrd data/mri_planes.csv data/mri_demo.mp4
```

The program successfully generated four frames and an MP4 video.

The signed 16-bit MRI dataset is an additional compatibility demonstration. The assignment's required data types are unsigned 8/16/32-bit.

## Testing

Correctness tests cover:

- Triangle dimensions and sampled values.
- Trapezoid dimensions and sampled values.
- Out-of-bounds voxel sampling.
- Invalid geometric definitions.
- EPS tolerance.
- Equal-length trapezoid bases.
- Non-finite coordinate rejection.
- Nearest-neighbor sampling.
- Unsigned 32-bit value preservation.

Latest recorded test results:

```text
Tests passed: 17
Tests failed: 0
```

To run the projection tests, execute the main method of `ProjectionTest` in IntelliJ IDEA.

The tests verify the implemented cases and do not provide exhaustive correctness guarantees.

## Performance Experiments

The Benchmark class performs warm-up operations followed by 100 projection repetitions.

| Dataset | Volume dimensions | Repetitions | Time | Projection FPS |
|---|---|---|---|---|
| MRI | 256 x 256 x 130 | 100 | 0.0164 s | 6107.23 |
| Synthetic volume | 300 x 300 x 300 | 100 | 0.0197 s | 5081.61 |
| Synthetic volume | 500 x 500 x 500 | 100 | 0.0439 s | 2278.80 |
These results measure projection computations only.

They exclude image rendering, MP4 encoding, and file loading.

Both benchmarks repeatedly use the same triangular projection.

Therefore, these results do not yet prove end-to-end performance above 30 FPS for a 500 x 500 x 500 dataset.

## Error Handling

- Invalid geometric configurations cause exceptions.
- Invalid CSV records produce visually distinct error frames.
- The input CSV order is preserved in the output video.
- Unsupported or malformed NRRD files cause input errors.

## Project Documentation

Additional documents are located in the `docs` directory:

- `SRS.md` - Software Requirements Specification.
- `Algorithm.md` - Projection algorithm description.
- `Design.md` - Object-oriented software design.
- `Experiment.md` - Testing strategy and experimental results.

## Known Limitations

- Projection-only performance was measured on a
  500 x 500 x 500 synthetic volume, but full
  end-to-end performance above 30 FPS has not
  yet been demonstrated.
- Image rendering and MP4 encoding require
  separate performance evaluation.
- Grayscale normalization may vary between frames.
- The NRRD reader implements a supported subset
  of the complete NRRD specification.
- The MRI dataset is stored locally and is not
  included in the GitHub repository.

### Building with IntelliJ IDEA

Open the project in IntelliJ IDEA and import
it as a Maven project.

Select JDK 21.

Open the Maven tool window and run:

Lifecycle -> clean
Lifecycle -> compile

The project can then be executed using an
Application run configuration with Main as
the main class.

## Academic Context

Object-Oriented Programming - Assignment 1

Ariel University
