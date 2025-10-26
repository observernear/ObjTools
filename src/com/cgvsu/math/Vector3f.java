package com.cgvsu.math;

import java.util.Objects;

public class Vector3f {
    private static final float EPSILON = 1e-7f;
    private final float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector3f other = (Vector3f) obj;
        return Math.abs(x - other.x) < EPSILON &&
                Math.abs(y - other.y) < EPSILON &&
                Math.abs(z - other.z) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Float.hashCode(x),
                Float.hashCode(y),
                Float.hashCode(z)
        );
    }

    public boolean equals(Vector3f other) {
        if (other == null) return false;
        return Math.abs(x - other.x) < EPSILON &&
                Math.abs(y - other.y) < EPSILON &&
                Math.abs(z - other.z) < EPSILON;
    }

    @Override
    public String toString() {
        return String.format("Vector3f(%.6f, %.6f, %.6f)", x, y, z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}