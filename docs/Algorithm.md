
# Plane Projection Algorithm

## 1. Goal

The purpose of the projection algorithm is to extract
a two-dimensional image from a three-dimensional
raster volume.

The input consists of:

- A three-dimensional volume M.
- Three points defining an isosceles triangle,
  or four points defining an isosceles trapezoid.

The output is a two-dimensional array E containing
voxel values sampled from the specified region.

The implementation follows the row-by-row projection
approach described in the assignment.

## 2. Input Volume

The input volume has dimensions:

M[sizeX][sizeY][sizeZ]

Each dimension satisfies:

1 <= sizeX, sizeY, sizeZ < 1024

The required voxel formats are unsigned 8-bit,
16-bit, and 32-bit integers.

Internally, Volume3D stores values in a flattened
int array.

Unsigned values are returned using:

Integer.toUnsignedLong(value)

The flattened array index is:

index = (z * sizeY + y) * sizeX + x

This allows constant-time voxel access.

## 3. Three-Point Isosceles Triangle

### 3.1 Geometric Definition

A triangle is defined by three points:

A = (ax, ay, az)
B = (bx, by, bz)
C = (cx, cy, cz)

The triangle must satisfy:

dist(A,B) = dist(A,C)

The implementation checks approximate equality:

abs(dist(A,B) - dist(A,C)) < EPS

where:

EPS = 0.001

The three points must also define a nondegenerate
geometric plane.

### 3.2 Distance Formula

The Euclidean distance between two points
P and Q is:

dist(P,Q) =
sqrt((Px-Qx)^2 + (Py-Qy)^2 + (Pz-Qz)^2)

Define:

Dab = dist(A,B)
Dac = dist(A,C)
Dbc = dist(B,C)

For a valid isosceles triangle:

Dab is approximately equal to Dac.

### 3.3 Output Image Dimensions

The reference algorithm describes output dimensions
approximately equal to:

Dab x Dbc

The current Java implementation uses:

height = ceil(Dab) + 1
width  = ceil(Dbc) + 1

The additional endpoint samples include both ends
of the sampled sides.

The result is stored in a long[][] array.

### 3.4 Interpolation

The algorithm moves along sides AB and AC.

For each output row y:

t = y / (height - 1)

The corresponding points on the sides are:

L(t) = A + t * (B - A)

R(t) = A + t * (C - A)

At t = 0:

L(0) = R(0) = A

At t = 1:

L(1) = B
R(1) = C

Therefore, the sampled segment grows from the
triangle vertex toward its opposite base.

### 3.5 Row Sampling

For each row, calculate:

rowLength = dist(L(t), R(t))

The implementation chooses:

samples = ceil(rowLength) + 1

The row is centered in the output image:

startX = (width - samples) / 2

For each sample x:

s = x / (samples - 1)

The sampled three-dimensional point is:

P = L(t) + s * (R(t) - L(t))

When samples = 1, the implementation uses s = 0
to avoid division by zero.

Each sampled voxel value is stored at:

E[y][startX + x]

subject to valid image boundaries.

Positions not assigned during row sampling
remain zero.

## 4. Four-Point Isosceles Trapezoid

### 4.1 Geometric Definition

A trapezoid is defined by four points:

A1, A2, B, C

The two bases are:

A1-A2
B-C

The two legs are:

A1-B
A2-C

The required geometric constraints include:

abs(dist(A1,B) - dist(A2,C)) < EPS

dist(A1,A2) <= dist(B,C)

All four points must lie in the same plane.

The implementation additionally validates
parallel base directions and rejects
degenerate configurations.

### 4.2 Output Dimensions

Define:

sideLength = dist(A1,B)
lowerWidth = dist(B,C)

The implementation uses:

height = max(2, ceil(sideLength) + 1)

width = max(1, ceil(lowerWidth) + 1)

Since the upper base cannot exceed the lower base,
the lower base determines the maximum output width.

### 4.3 Interpolation

For each output row y:

t = y / (height - 1)

The endpoints of that row are:

L(t) = A1 + t * (B - A1)

R(t) = A2 + t * (C - A2)

At t = 0, the row spans the upper base A1-A2.

At t = 1, the row spans the lower base B-C.

The algorithm samples the segment between these
points using the same row-centering approach
as the triangular projection.

## 5. Nearest-Neighbor Voxel Sampling

For a point:

P = (px, py, pz)

The selected voxel coordinates are:

x = round(px)
y = round(py)
z = round(pz)

If the rounded coordinates are inside the volume,
the algorithm returns:

M[x,y,z]

Otherwise, it returns:

0

This implements the nearest-voxel sampling method
permitted by the assignment.

No interpolation between voxel intensities or
super-resolution is performed.

## 6. Algorithm Pseudocode

### 6.1 Triangle Projection

FUNCTION projectTriangle(M, A, B, C):

    validateTriangle(A, B, C)

    height = ceil(dist(A,B)) + 1
    width = ceil(dist(B,C)) + 1

    create zero-filled E[height][width]

    FOR y = 0 TO height - 1:

        t = y / (height - 1)

        left = interpolate(A, B, t)
        right = interpolate(A, C, t)

        rowLength = dist(left, right)
        samples = ceil(rowLength) + 1

        startX = (width - samples) / 2

        FOR x = 0 TO samples - 1:

            IF samples == 1:
                s = 0
            ELSE:
                s = x / (samples - 1)

            p = interpolate(left, right, s)

            value = sampleNearest(M, p)

            imageX = startX + x

            IF imageX is inside the image:
                E[y][imageX] = value

    RETURN E

### 6.2 Trapezoid Projection

FUNCTION projectTrapezoid(M, A1, A2, B, C):

    validateTrapezoid(A1, A2, B, C)

    height = max(2, ceil(dist(A1,B)) + 1)
    width = max(1, ceil(dist(B,C)) + 1)

    create zero-filled E[height][width]

    FOR y = 0 TO height - 1:

        t = y / (height - 1)

        left = interpolate(A1, B, t)
        right = interpolate(A2, C, t)

        rowLength = dist(left, right)
        samples = ceil(rowLength) + 1

        startX = (width - samples) / 2

        FOR x = 0 TO samples - 1:

            IF samples == 1:
                s = 0
            ELSE:
                s = x / (samples - 1)

            p = interpolate(left, right, s)

            imageX = startX + x

            IF imageX is inside the image:
                E[y][imageX] = sampleNearest(M, p)

    RETURN E

## 7. Error Handling

Invalid geometric input causes an
IllegalArgumentException.

Examples include:

- Collinear triangle points.
- Unequal triangle sides beyond EPS.
- Non-coplanar trapezoid points.
- Unequal trapezoid legs.
- Invalid trapezoid base lengths.
- Degenerate geometric configurations.

The projection functions report these errors.

The FrameGenerator class catches individual
plane errors and generates a visually distinct
error frame.

Later CSV records are still processed.

The output frame order remains consistent
with the CSV input order.

## 8. Grayscale Visualization

The projection algorithm returns numerical
voxel values.

These values are separate from the grayscale
values used to display the resulting images.

FrameGenerator maps projection values into
the range 0 to 255.

The current visualization:

1. Finds the minimum and maximum nonzero values
   within each projection.
2. Keeps zero-valued pixels black.
3. Maps the remaining values to grayscale.
4. Resizes the image to fit a 320 x 240 frame.
5. Preserves the original aspect ratio.
6. Uses nearest-neighbor image resizing.

This normalization is used for visualization
and does not change the voxel values returned
by PlaneProjector.

A limitation is that zero-valued sampled voxels
cannot be distinguished from unfilled pixels
by the current visualization process.

Because normalization is performed separately
for each frame, grayscale brightness may vary
between frames.

## 9. Time Complexity

Define:

H = output image height
W = output image width

### 9.1 Voxel Access

Voxel access in Volume3D uses direct indexing
into a flattened array.

Therefore:

T_access = O(1)

### 9.2 Projection

The algorithm processes H output rows.

Each row contains at most approximately W samples.

Therefore, the upper bound on projection time is:

T_projection = O(H * W)

For triangles, the actual number of sampled pixels
grows gradually from the vertex toward the base.

For trapezoids, the number of sampled pixels grows
from the upper base toward the lower base.

Both variants have worst-case time complexity:

O(H * W)

The algorithm does not scan the entire input
volume for every projection.

### 9.3 Space Complexity

The generated projection is stored in a
two-dimensional array of dimensions H x W.

Therefore:

S_projection = O(H * W)

The input volume itself is stored separately.

With an int array, voxel storage requires
approximately four bytes per voxel.

Thus:

S_volume = O(sizeX * sizeY * sizeZ)

For a 500 x 500 x 500 volume, the underlying
int array requires approximately 500 MB,
excluding Java object and runtime overhead.

## 10. Implementation Classes

The projection algorithm depends on the following
classes:

- Point3D: Represents a 3D coordinate.
- Vector3D: Provides vector calculations.
- Plane3D: Validates geometric definitions.
- Volume3D: Stores and retrieves voxel values.
- PlaneProjector: Computes 2D projections.
- FrameGenerator: Converts projections to frames.
- VideoWriter: Encodes the final MP4 video.

This separation allows geometric validation,
voxel sampling, visualization, and video encoding
to be tested independently.

## 11. Correctness Testing

The projection implementation was tested using
synthetic volumes with known voxel values.

Tests include:

- Triangle image dimensions.
- Trapezoid image dimensions.
- Expected sampled voxel values.
- Out-of-bounds sampling.
- Invalid triangle rejection.
- Invalid trapezoid rejection.
- EPS tolerance behavior.
- Equal-length trapezoid bases.
- Non-finite coordinate rejection.
- Nearest-neighbor sampling.
- Preservation of unsigned 32-bit values.

The latest recorded test result was:

Tests passed: 17
Tests failed: 0

These tests provide evidence for the tested
cases but do not prove correctness for all
possible geometric configurations.

## 12. Performance Measurements

The Benchmark class performs:

- 20 warm-up projections.
- 100 measured projections.

Recorded results:

| Volume | Repetitions | Time | Projection rate |
|---|---:|---:|---:|
| 256 x 256 x 130 | 100 | 0.0164 s | 6107.23/s |
| 300 x 300 x 300 | 100 | 0.0197 s | 5081.61/s |

These benchmarks measure projection computation
without NRRD reading, grayscale frame generation,
or MP4 encoding.

The same geometric region was used repeatedly.

The results therefore do not establish
end-to-end FPS compliance for a 500^3 volume.

## 13. Limitations and Future Improvements

The current implementation has several limitations:

- The projection method creates temporary Point3D
  objects during sampling.
- Grayscale normalization is performed independently
  for each output frame.
- Zero-valued voxels and unfilled projection pixels
  share the same numerical representation.
- FrameGenerator stores generated BufferedImage
  frames in a list before video encoding.
- End-to-end throughput may be lower than
  projection-only benchmark throughput.
- Performance with varying large projections and
  a 500 x 500 x 500 input still requires measurement.

Potential improvements include reducing temporary
object allocations, separating image masks from
voxel values, streaming frames directly to the video
encoder, and expanding performance tests.

## 14. Conclusion

The algorithm extracts triangular and trapezoidal
two-dimensional regions from a three-dimensional
volume using row-by-row interpolation and
nearest-neighbor voxel sampling.

The implementation follows the overall projection
method described in the assignment.

It emphasizes modular object-oriented design,
input validation, and efficient random access
to voxel values.

The measured projection-only performance is
promising, while complete correctness and
end-to-end performance require additional
verification.
