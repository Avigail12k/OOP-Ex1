
# Software Requirements Specification (SRS)

## 1. Introduction

This document defines the software requirements
for the 3D Volume Plane Projection project.

The system will be implemented in Java 21.

## 2. Input Requirements

### FR-01: NRRD Volume Input
The system shall read a three-dimensional
volume from an NRRD file.

The volume contains unsigned integer values
represented using 8, 16, or 32 bits.

### FR-02: CSV Plane Input
The system shall read a CSV file containing
an ordered list of planes.

Each plane is represented by 3 or 4
points in three-dimensional space.

## 3. Processing Requirements

### FR-03: Plane Validation
The system shall validate each plane definition.

For three-point definitions, the system shall
reject collinear points, since they do not
define a unique plane.

For four-point definitions, the system shall
verify that all four points are coplanar
and satisfy the assignment's additional
geometric constraints.

The validation policy shall document how
the tolerance EPS = 0.001 is applied.

Invalid plane definitions shall raise an exception.
The demo application shall catch these exceptions
and continue processing subsequent planes.

### FR-04: Plane Sampling
For each valid plane, the system shall
generate a two-dimensional image by
sampling values from the 3D volume.

Nearest-neighbor sampling will be used.

### FR-05: Error Frames
For an invalid plane, the demo shall generate
an error frame instead of a regular image.

## 4. Output Requirements

### FR-06: MP4 Video
The system shall create an MP4 video.

The video shall contain one frame per plane,
in the same order as the CSV input.

## 5. Non-Functional Requirements

### NFR-01: Correctness
The system shall produce correct 2D projections.

### NFR-02: Robustness
Invalid plane definitions shall not terminate
the entire demo.

### NFR-03: Performance
For a volume of 500^3 bytes, the system
shall aim for at least 100 frames per second.

A rate above 30 FPS is a mandatory minimum
according to the assignment instructions.


### NFR-04: Maintainability
The implementation shall use a modular,
object-oriented design with clearly
defined responsibilities.

## 6. Testing Requirements

The system shall be tested using multiple
NRRD volumes and CSV plane definitions.

Tests shall include valid inputs, invalid
planes, boundary cases, and performance tests.
