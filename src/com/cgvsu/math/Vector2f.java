package com.cgvsu.math;

public class Vector2f {
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Vector2f other) {
        final float eps = 1e-7f;
        return Math.abs(x - other.x) < eps && Math.abs(y - other.y) < eps;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private final float x, y;
}