package com.cgvsu.util;

import java.io.IOException;

public interface FileCompareInt {
    void compareFiles() throws IOException;
    boolean areFilesIdentical() throws IOException;
    void printDifferenceSummary() throws IOException;
    void compareFilesContent() throws IOException;
}