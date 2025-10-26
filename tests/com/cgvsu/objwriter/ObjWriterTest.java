package com.cgvsu.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ObjWriterTest {

    @TempDir
    Path tempDir;

    // Основная функциональность - корректный вывод формата
    @Test
    public void testBasicOutputFormat() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1.0f, 2.0f, 3.0f));
        model.getVertices().add(new Vector3f(4.0f, 5.0f, 6.0f));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 0)));
        model.getPolygons().add(polygon);

        String result = ObjWriter.modelToString(model);

        assertTrue(result.contains("v "), "Should contain vertices");
        assertTrue(result.contains("f "), "Should contain faces");
        assertTrue(result.startsWith("#"), "Should start with comment");
    }

    // Форматирование чисел
    @Test
    public void testNumberFormatting() {
        assertEquals("1", ObjWriter.formatFloatCompact(1.0f));
        assertEquals("1.5", ObjWriter.formatFloatCompact(1.5f));
        assertEquals("1.23456", ObjWriter.formatFloatCompact(1.23456f));
        assertEquals("0.001", ObjWriter.formatFloatCompact(0.001f));
        assertEquals("0", ObjWriter.formatFloatCompact(0.0f));
    }

    // Различные комбинации данных
    @Test
    public void testVertexOnlyOutput() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1.0f, 0.0f, 0.0f));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        model.getPolygons().add(polygon);

        String result = ObjWriter.modelToString(model);
        assertTrue(result.contains("v 1 0 0"));
        assertTrue(result.contains("f 1 1 1"));
        assertFalse(result.contains("/"), "Should not have texture/normal separators");
    }

    @Test
    public void testVertexWithTextureOutput() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1.0f, 0.0f, 0.0f));
        model.getTextureVertices().add(new Vector2f(0.5f, 0.5f));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        polygon.setTextureVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        model.getPolygons().add(polygon);

        String result = ObjWriter.modelToString(model);
        assertTrue(result.contains("vt 0.5 0.5"));
        assertTrue(result.contains("f 1/1"), "Should have vertex/texture format");
    }

    @Test
    public void testVertexWithNormalOutput() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1.0f, 0.0f, 0.0f));
        model.getNormals().add(new Vector3f(0.0f, 1.0f, 0.0f));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        polygon.setNormalIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        model.getPolygons().add(polygon);

        String result = ObjWriter.modelToString(model);
        assertTrue(result.contains("vn 0 1 0"));
        assertTrue(result.contains("f 1//1"), "Should have vertex//normal format");
    }

    @Test
    public void testAllComponentsOutput() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1.0f, 0.0f, 0.0f));
        model.getTextureVertices().add(new Vector2f(0.5f, 0.5f));
        model.getNormals().add(new Vector3f(0.0f, 1.0f, 0.0f));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        polygon.setTextureVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        polygon.setNormalIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        model.getPolygons().add(polygon);

        String result = ObjWriter.modelToString(model);
        assertTrue(result.contains("f 1/1/1"), "Should have vertex/texture/normal format");
    }

    // запись в файл
    @Test
    public void testFileWriting() throws IOException {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1.0f, 2.0f, 3.0f));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        model.getPolygons().add(polygon);

        Path filePath = tempDir.resolve("test.obj");
        ObjWriter.write(model, filePath.toString());

        assertTrue(Files.exists(filePath));
        String content = Files.readString(filePath);
        assertTrue(content.contains("v 1 2 3"));
    }

    // Обработка ошибок ВАЛИДАЦИИ
    @Test
    public void testNullModelHandling() {
        ObjWriterException exception = assertThrows(ObjWriterException.class,
                () -> ObjWriter.modelToString(null));
        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    public void testNaNValueHandling() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(Float.NaN, 0.0f, 0.0f));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        model.getPolygons().add(polygon);

        ObjWriterException exception = assertThrows(ObjWriterException.class,
                () -> ObjWriter.modelToString(model));
        assertTrue(exception.getMessage().contains("NaN"));
    }

    // Индексация (1-based в OBJ vs 0-based в Java)
    @Test
    public void testIndexing() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1.0f, 0.0f, 0.0f)); // index 0
        model.getVertices().add(new Vector3f(0.0f, 1.0f, 0.0f)); // index 1
        model.getVertices().add(new Vector3f(0.0f, 0.0f, 1.0f)); // index 2

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2))); // 0-based
        model.getPolygons().add(polygon);

        String result = ObjWriter.modelToString(model);
        // Должны стать 1-based в выводе
        assertTrue(result.contains("f 1 2 3"));
    }

    // Порядок данных в выводе
    @Test
    public void testOutputOrder() {
        Model model = new Model();
        // Добавляем в разном порядке
        model.getNormals().add(new Vector3f(0.0f, 0.0f, 1.0f));
        model.getVertices().add(new Vector3f(1.0f, 0.0f, 0.0f));
        model.getTextureVertices().add(new Vector2f(0.5f, 0.5f));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        polygon.setTextureVertexIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        polygon.setNormalIndices(new ArrayList<>(Arrays.asList(0, 0, 0)));
        model.getPolygons().add(polygon);

        String result = ObjWriter.modelToString(model);

        // Проверяем порядок: vertices -> textures -> normals -> faces
        int vertexPos = result.indexOf("v ");
        int texturePos = result.indexOf("vt ");
        int normalPos = result.indexOf("vn ");
        int facePos = result.indexOf("f ");

        assertTrue(vertexPos < texturePos, "Vertices should come before textures");
        assertTrue(texturePos < normalPos, "Textures should come before normals");
        assertTrue(normalPos < facePos, "Normals should come before faces");
    }

    // Пустая модель
    @Test
    public void testEmptyModel() {
        Model model = new Model();

        String result = ObjWriter.modelToString(model);
        assertNotNull(result);
        assertTrue(result.contains("Exported by"));
        assertFalse(result.contains("v "), "No vertices in empty model");
        assertFalse(result.contains("f "), "No faces in empty model");
    }
}