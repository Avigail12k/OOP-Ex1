
# Plane Projection Algorithm

## 1. Goal

Given a 3D volume M and a plane defined by
3 or 4 points, generate a 2D image E.

## 2. Reference Algorithm

The assignment provides a projection algorithm
based on an isosceles triangle.

For three points a, b, c:

dist(a,b) = dist(a,c)

Define:
- Dab = dist(a,b)
- Dbc = dist(b,c)
- u = normalize(b-a)
- v = normalize(c-a)

The algorithm moves along the two equal-length
sides of the triangle.

For each step, it samples the line segment
between the current points on these sides.

The sampled voxel values are placed into
the corresponding row of the output image.

Values outside the volume are set to zero.

## 3. Four-Point Case

The assignment also describes an extension
for an isosceles trapezoid defined by four points.

The implementation must validate the required
geometric constraints before projection.

## 4. Implementation Decisions

The implementation will:
- Use nearest-neighbor voxel sampling.
- Handle floating-point coordinates.
- Convert image dimensions to integer sizes.
- Handle degenerate geometric cases.
- Return zero for samples outside the volume.
- Generate error frames for invalid planes.

## 5. Performance

The projection algorithm should avoid
unnecessary processing of the entire volume.

Runtime performance will be measured
using frames per second (FPS).

## 6. Status

Initial design based on the projection algorithm
provided in the assignment.

Detailed formulas, boundary rules, and
implementation tests are still required.
