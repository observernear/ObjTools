package com.cgvsu.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ObjWriter {

    public static void write(Model model, String filePath) throws IOException {
        String content = modelToString(model);
        Files.writeString(Path.of(filePath), content);
    }

    public static String modelToString(Model model) {
        return modelToString(model, "Exported by Lapin Nikita ObjWriter");
    }

    public static String modelToString(Model model, String comment) {
        if (model == null) {
            throw new ObjWriterException("Model cannot be null");
        }

        StringBuilder sb = new StringBuilder();

        if (comment != null && !comment.isEmpty()) {
            sb.append("# ").append(comment).append("\n");
        }
//        sb.append("# Vertices: ").append(model.vertices.size()).append("\n");
//        sb.append("# Texture vertices: ").append(model.textureVertices.size()).append("\n");
//        sb.append("# Normals: ").append(model.normals.size()).append("\n");
//        sb.append("# Polygons: ").append(model.polygons.size()).append("\n\n");

        try {
            for (Vector3f vertex : model.vertices) {
                validateVertex(vertex, model.vertices.indexOf(vertex));
                sb.append("v ")
                        .append(formatFloatCompact(vertex.getX()))
                        .append(" ")
                        .append(formatFloatCompact(vertex.getY()))
                        .append(" ")
                        .append(formatFloatCompact(vertex.getZ()))
                        .append("\n");
            }

            if (!model.vertices.isEmpty() && (!model.textureVertices.isEmpty() || !model.normals.isEmpty())) {
                sb.append("\n");
            }

            for (Vector2f textureVertex : model.textureVertices) {
                validateTextureVertex(textureVertex, model.textureVertices.indexOf(textureVertex));
                sb.append("vt ")
                        .append(formatFloatCompact(textureVertex.getX()))
                        .append(" ")
                        .append(formatFloatCompact(textureVertex.getY()))
                        .append("\n");
            }

            if (!model.textureVertices.isEmpty() && !model.normals.isEmpty()) {
                sb.append("\n");
            }

            for (Vector3f normal : model.normals) {
                validateNormal(normal, model.normals.indexOf(normal));
                sb.append("vn ")
                        .append(formatFloatCompact(normal.getX()))
                        .append(" ")
                        .append(formatFloatCompact(normal.getY()))
                        .append(" ")
                        .append(formatFloatCompact(normal.getZ()))
                        .append("\n");
            }

            if ((!model.vertices.isEmpty() || !model.textureVertices.isEmpty() || !model.normals.isEmpty())
                    && !model.polygons.isEmpty()) {
                sb.append("\n");
            }

            for (Polygon polygon : model.polygons) {
                validatePolygon(polygon, model.polygons.indexOf(polygon),
                        model.vertices.size(), model.textureVertices.size(), model.normals.size());

                sb.append("f");
                List<Integer> vertexIndices = polygon.getVertexIndices();
                List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
                List<Integer> normalIndices = polygon.getNormalIndices();

                for (int i = 0; i < vertexIndices.size(); i++) {
                    sb.append(" ");
                    sb.append(vertexIndices.get(i) + 1);

                    if (!textureVertexIndices.isEmpty() || !normalIndices.isEmpty()) {
                        sb.append("/");

                        if (!textureVertexIndices.isEmpty()) {
                            sb.append(textureVertexIndices.get(i) + 1);
                        }

                        if (!normalIndices.isEmpty()) {
                            sb.append("/").append(normalIndices.get(i) + 1);
                        }
                    }
                }
                sb.append("\n");
            }

        } catch (IndexOutOfBoundsException e) {
            throw new ObjWriterException("Invalid model data structure", e);
        } catch (NullPointerException e) {
            throw new ObjWriterException("Model contains null elements", e);
        }

        return sb.toString();
    }

    //компактное форматирование чисел как в оригинальном файле
    protected static String formatFloatCompact(float value) {
        if (Float.isNaN(value)) {
            throw new ObjWriterException("Cannot format NaN value");
        }
        if (Float.isInfinite(value)) {
            throw new ObjWriterException("Cannot format infinite value");
        }

        String result = String.format("%.6f", value).replace(",", ".");

        if (result.contains(".")) {
            result = result.replaceAll("0*$", "");
            if (result.endsWith(".")) {
                result = result.substring(0, result.length() - 1);
            }
        }

        return result;
    }

    // Обычные валидаторы

    protected static void validateVertex(Vector3f vertex, int index) {
        if (vertex == null) {
            throw new ObjWriterException("Vertex at index " + index + " is null");
        }
        if (Float.isNaN(vertex.getX()) || Float.isNaN(vertex.getY()) || Float.isNaN(vertex.getZ())) {
            throw new ObjWriterException("Vertex at index " + index + " contains NaN values");
        }
        if (Float.isInfinite(vertex.getX()) || Float.isInfinite(vertex.getY()) || Float.isInfinite(vertex.getZ())) {
            throw new ObjWriterException("Vertex at index " + index + " contains infinite values");
        }
    }

    protected static void validateTextureVertex(Vector2f textureVertex, int index) {
        if (textureVertex == null) {
            throw new ObjWriterException("Texture vertex at index " + index + " is null");
        }
        if (Float.isNaN(textureVertex.getX()) || Float.isNaN(textureVertex.getY())) {
            throw new ObjWriterException("Texture vertex at index " + index + " contains NaN values");
        }
        if (Float.isInfinite(textureVertex.getX()) || Float.isInfinite(textureVertex.getY())) {
            throw new ObjWriterException("Texture vertex at index " + index + " contains infinite values");
        }
    }

    protected static void validateNormal(Vector3f normal, int index) {
        if (normal == null) {
            throw new ObjWriterException("Normal at index " + index + " is null");
        }
        if (Float.isNaN(normal.getX()) || Float.isNaN(normal.getY()) || Float.isNaN(normal.getZ())) {
            throw new ObjWriterException("Normal at index " + index + " contains NaN values");
        }
        if (Float.isInfinite(normal.getX()) || Float.isInfinite(normal.getY()) || Float.isInfinite(normal.getZ())) {
            throw new ObjWriterException("Normal at index " + index + " contains infinite values");
        }
    }

    protected static void validatePolygon(Polygon polygon, int polyIndex, int vertexCount,
                                        int textureVertexCount, int normalCount) {
        if (polygon == null) {
            throw new ObjWriterException("Polygon at index " + polyIndex + " is null");
        }

        List<Integer> vertexIndices = polygon.getVertexIndices();
        List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
        List<Integer> normalIndices = polygon.getNormalIndices();

        if (vertexIndices == null) {
            throw new ObjWriterException("Polygon at index " + polyIndex + " has null vertex indices");
        }

        if (vertexIndices.isEmpty()) {
            throw new ObjWriterException("Polygon at index " + polyIndex + " has no vertices");
        }

        if (vertexIndices.size() < 3) {
            throw new ObjWriterException("Polygon at index " + polyIndex + " has less than 3 vertices");
        }

        for (int vertexIndex : vertexIndices) {
            if (vertexIndex < 0 || vertexIndex >= vertexCount) {
                throw new ObjWriterException(
                        "Polygon at index " + polyIndex + " references invalid vertex index " +
                                vertexIndex + " (available vertices: 0-" + (vertexCount - 1) + ")"
                );
            }
        }

        if (!textureVertexIndices.isEmpty() && textureVertexIndices.size() != vertexIndices.size()) {
            throw new ObjWriterException(
                    "Polygon at index " + polyIndex + " has mismatched vertex and texture vertex counts"
            );
        }

        for (int texIndex : textureVertexIndices) {
            if (texIndex < 0 || texIndex >= textureVertexCount) {
                throw new ObjWriterException(
                        "Polygon at index " + polyIndex + " references invalid texture vertex index " +
                                texIndex + " (available texture vertices: 0-" + (textureVertexCount - 1) + ")"
                );
            }
        }

        if (!normalIndices.isEmpty() && normalIndices.size() != vertexIndices.size()) {
            throw new ObjWriterException(
                    "Polygon at index " + polyIndex + " has mismatched vertex and normal counts"
            );
        }

        for (int normalIndex : normalIndices) {
            if (normalIndex < 0 || normalIndex >= normalCount) {
                throw new ObjWriterException(
                        "Polygon at index " + polyIndex + " references invalid normal index " +
                                normalIndex + " (available normals: 0-" + (normalCount - 1) + ")"
                );
            }
        }
    }
}