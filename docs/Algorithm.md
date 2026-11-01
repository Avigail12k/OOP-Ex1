
# Algorithm Design

## 1. Overview

The algorithm generates a 2D image from a 3D volume
using a plane defined by 3 or 4 points.

## 2. Main Steps

1. Load the 3D volume from an NRRD file.
2. Read plane definitions from the CSV file.
3. Validate each plane.
4. Construct a local 2D coordinate system on the plane.
5. Determine the image bounds and sampling resolution.
6. Map each image pixel to a point in the 3D volume.
7. Sample the nearest voxel.
8. Store the resulting 2D image.
9. Generate an error frame for an invalid plane.
10. Combine all frames into an MP4 video.

## 3. Design Decisions

The exact image dimensions, sampling spacing,
coordinate conventions, and boundary handling
will be specified before implementation.
