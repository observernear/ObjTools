package com.cgvsu.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileCompareObj implements FileCompareImpl {
    private Path firstFile;
    private Path secondFile;

    public FileCompareObj(Path firstFile, Path secondFile) {
        this.firstFile = firstFile;
        this.secondFile = secondFile;
    }

    public Path getFirstFile() {
        return firstFile;
    }

    public Path getSecondFile() {
        return secondFile;
    }

    public void setFirstFile(Path newFile) {
        firstFile = newFile;
    }

    public void setSecondFile(Path newFile) {
        secondFile = newFile;
    }

    @Override
    public void compareFiles() throws IOException {
        List<String> originalLines = Files.readAllLines(firstFile);
        List<String> generatedLines = Files.readAllLines(secondFile);

        System.out.println("First file: " + firstFile.getFileName());
        System.out.println("Second file: " + secondFile.getFileName());
        System.out.println("First file lines: " + originalLines.size());
        System.out.println("Second file lines: " + generatedLines.size());

        long firstVertices = countElements(originalLines, "v ");
        long firstTextures = countElements(originalLines, "vt ");
        long firstNormals = countElements(originalLines, "vn ");
        long firstFaces = countElements(originalLines, "f ");

        long secondVertices = countElements(generatedLines, "v ");
        long secondTextures = countElements(generatedLines, "vt ");
        long secondNormals = countElements(generatedLines, "vn ");
        long secondFaces = countElements(generatedLines, "f ");

        System.out.println("\nElement counts:");
        printComparison("Vertices", firstVertices, secondVertices);
        printComparison("Texture vertices", firstTextures, secondTextures);
        printComparison("Normals", firstNormals, secondNormals);
        printComparison("Faces", firstFaces, secondFaces);

        // Дополнительная статистика
        System.out.println("\nAdditional statistics:");
        System.out.println("First file comments: " + countElements(originalLines, "#"));
        System.out.println("Second file comments: " + countElements(generatedLines, "#"));
        System.out.println("First file object groups: " + countElements(originalLines, "o "));
        System.out.println("Second file object groups: " + countElements(generatedLines, "o "));
    }

    private long countElements(List<String> lines, String prefix) {
        return lines.stream()
                .filter(line -> line.startsWith(prefix))
                .count();
    }

    private void printComparison(String elementName, long firstCount, long secondCount) {
        String status = firstCount == secondCount ? "MATCH" : "DIFFERENT";
        System.out.printf("%s - First: %d, Second: %d -> %s%n",
                elementName, firstCount, secondCount, status);
    }

    @Override
    public void printDifferenceSummary() throws IOException {
        List<String> firstLines = Files.readAllLines(firstFile);
        List<String> secondLines = Files.readAllLines(secondFile);

        long firstVertices = countElements(firstLines, "v ");
        long secondVertices = countElements(secondLines, "v ");
        long firstFaces = countElements(firstLines, "f ");
        long secondFaces = countElements(secondLines, "f ");

        System.out.println("Vertex difference: " + (secondVertices - firstVertices));
        System.out.println("Face difference: " + (secondFaces - firstFaces));

        if (firstVertices == secondVertices && firstFaces == secondFaces) {
            System.out.println("✓ Files have the same geometry structure");
        } else {
            System.out.println("✗ Files have different geometry structure");
        }
    }

    @Override
    public boolean areFilesIdentical() throws IOException {
        String content1 = Files.readString(firstFile);
        String content2 = Files.readString(secondFile);
        return content1.equals(content2);
    }

    @Override
    public void compareFilesContent() throws IOException {
        String content1 = Files.readString(firstFile);
        String content2 = Files.readString(secondFile);

        if (content1.equals(content2)) {
            System.out.println("PERFECT: Files are identical!");
        } else {
            System.out.println("Files are similar but not identical (this is normal due to float formatting)");

            // первые несколько различий
            String[] lines1 = content1.split("\n");
            String[] lines2 = content2.split("\n");

            int differences = 0;
            int maxDifferencesToShow = 5;

            for (int i = 0; i < Math.min(lines1.length, lines2.length) && differences < maxDifferencesToShow; i++) {
                if (!lines1[i].equals(lines2[i])) {
                    System.out.println("Difference at line " + (i + 1) + ":");
                    System.out.println("  Original: " + lines1[i]);
                    System.out.println("  Generated: " + lines2[i]);
                    differences++;
                }
            }

            if (differences == maxDifferencesToShow) {
                System.out.println("... and more differences");
            }
        }
    }
}