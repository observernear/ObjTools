package com.cgvsu.math;

import java.util.Objects;

public class Vector2f {
    private static final float EPSILON = 1e-7f;
    private final float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2f other = (Vector2f) obj;
        return Math.abs(x - other.x) < EPSILON &&
                Math.abs(y - other.y) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Float.hashCode(x),
                Float.hashCode(y)
        );
    }

    public boolean equals(Vector2f other) {
        if (other == null) return false;
        return Math.abs(x - other.x) < EPSILON &&
                Math.abs(y - other.y) < EPSILON;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Vector2f(%.6f, %.6f)", x, y);
    }
}