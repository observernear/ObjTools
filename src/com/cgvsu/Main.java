package com.cgvsu;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.util.FileCompareObj;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileObj = "WrapSkull";
        Path dataDir = Path.of("").toAbsolutePath().resolve("data");
        Path fileName = dataDir.resolve(fileObj + ".obj");

        String fileContent = Files.readString(fileName);

        System.out.println("Loading model ...");
        Model model = ObjReader.read(fileContent);

        System.out.println("Vertices: " + model.getVertices().size());
        System.out.println("Texture vertices: " + model.getTextureVertices().size());
        System.out.println("Normals: " + model.getNormals().size());
        System.out.println("Polygons: " + model.getPolygons().size());

        System.out.println("\nSaving model back...");

        Path outputFile = fileName.resolveSibling(String.format(Locale.ROOT, "%s_output.obj", fileObj));
        ObjWriter.write(model, outputFile.toString());

        System.out.println("Model saved to: " + outputFile);
        System.out.println("\n" + "=".repeat(50));

        FileCompareObj comparator = new FileCompareObj(fileName, outputFile);
        System.out.println("\n=== Basic File Comparison ===");
        comparator.compareFiles();

        System.out.println("\n=== Difference Summary ===");
        comparator.printDifferenceSummary();

//        System.out.println("\n=== Detailed Content Comparison ===");
//        comparator.compareFilesContent();
//
//        System.out.println("\n=== Final Check ===");
//        boolean identical = comparator.areFilesIdentical();
//        System.out.println("Files are completely identical: " + identical);
    }
}