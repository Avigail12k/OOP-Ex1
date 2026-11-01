
# Object-Oriented Design

## Overview

The project will use a modular object-oriented
design. Each class will have a clearly defined
responsibility.

## Proposed Classes

- Point3D: Represents a point in 3D space.
- Vector3D: Represents a 3D vector.
- Plane3D: Represents and validates a plane.
- Volume3D: Stores the 3D volume data.
- NrrdReader: Reads NRRD volume files.
- CsvReader: Reads plane definitions from CSV.
- PlaneProjector: Generates 2D images from planes.
- VideoWriter: Encodes frames into an MP4 video.
- Main: Coordinates the application execution.

## Design Principle

The design follows the Single Responsibility
Principle, separating geometry, file input,
image generation, and video output.

## Status

Initial design proposal. Class interfaces and
dependencies will be refined before implementation.
