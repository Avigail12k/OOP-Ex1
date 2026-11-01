
# 3D Volume Plane Projection

## Project Overview

This project is developed in Java as part of the
Object-Oriented Programming course at Ariel University.

The goal is to design and implement an algorithm
that extracts a 2D image from a 3D volume using
a plane defined by 3 or 4 points.

The project focuses on software design,
correctness, robustness, and runtime efficiency.

## Problem Definition

The input consists of:
- A 3D volume stored in NRRD format.
- A CSV file containing plane definitions.

Each plane is defined by 3 or 4 points in 3D space.

For each valid plane, the program will generate
a 2D image by sampling values from the volume.

The images will be combined into an MP4 video
in the same order as the planes in the CSV file.

## Project Scope

The project will include:
- Reading and validating NRRD volume data.
- Reading plane definitions from CSV files.
- Calculating 2D slices of the volume.
- Handling invalid plane definitions.
- Creating an MP4 video.
- Testing correctness and measuring performance.

## Implementation

Programming language: Java 21

Development environment: IntelliJ IDEA

Status: Initial design phase.

## Documentation

A detailed software requirements specification,
algorithm design, testing strategy, and experiment
results will be added as the project progresses.
