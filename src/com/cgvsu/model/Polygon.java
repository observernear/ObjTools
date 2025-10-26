package com.cgvsu.model;

import java.util.ArrayList;

public class Polygon {

    private ArrayList<Integer> vertexIndices;
    private ArrayList<Integer> textureVertexIndices;
    private ArrayList<Integer> normalIndices;


    public Polygon() {
        vertexIndices = new ArrayList<Integer>();
        textureVertexIndices = new ArrayList<Integer>();
        normalIndices = new ArrayList<Integer>();
    }

    public void setVertexIndices(ArrayList<Integer> vertexIndices) {
        if (vertexIndices == null) {
            throw new IllegalArgumentException("Vertex indices cannot be null");
        }
        if (vertexIndices.size() < 3) {
            throw new IllegalArgumentException("Polygon must have at least 3 vertices, got: " + vertexIndices.size());
        }
        this.vertexIndices = vertexIndices;
    }

    public void setTextureVertexIndices(ArrayList<Integer> textureVertexIndices) {
        if (textureVertexIndices != null && textureVertexIndices.size() < 3) {
            throw new IllegalArgumentException("Texture vertex indices must have at least 3 elements if provided, got: " + textureVertexIndices.size());
        }
        this.textureVertexIndices = textureVertexIndices;
    }

    public void setNormalIndices(ArrayList<Integer> normalIndices) {
        if (normalIndices != null && normalIndices.size() < 3) {
            throw new IllegalArgumentException("Normal indices must have at least 3 elements if provided, got: " + normalIndices.size());
        }
        this.normalIndices = normalIndices;
    }

    public ArrayList<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public ArrayList<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public ArrayList<Integer> getNormalIndices() {
        return normalIndices;
    }
}
