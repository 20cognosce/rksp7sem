package ru.mirea.prac2;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Task2 {
    /*
    1) FileInputStream/FileOutputStream
    2) FileChannel
    3) Apache Commons IO
    4) Files class
    */
    public static final Path path = Path.of(System.getProperty("user.dir") + "/src/main/resources/");
    public static final Path pdf = Path.of(System.getProperty("user.dir") + "/src/main/resources/voina-i-mir.pdf");

    @SneakyThrows
    Task2() {
        Files.deleteIfExists(Path.of(path + "/voina-i-mir-1.pdf"));
        Files.deleteIfExists(Path.of(path + "/voina-i-mir-2.pdf"));
        Files.deleteIfExists(Path.of(path + "/voina-i-mir-3.pdf"));
        Files.deleteIfExists(Path.of(path + "/voina-i-mir-4.pdf"));
    }

    @SneakyThrows
    void run() {
        long startTime, endTime;

        // Method 1: FileInputStream/FileOutputStream
        startTime = System.currentTimeMillis();
        copyFileUsingFileStreams(path + "/voina-i-mir-1.pdf");
        endTime = System.currentTimeMillis();
        System.out.println("Method 1 Time (ms): " + (endTime - startTime));
        measureMemoryUsage();

        // Method 2: FileChannel
        startTime = System.currentTimeMillis();
        copyFileUsingFileChannel(path + "/voina-i-mir-2.pdf");
        endTime = System.currentTimeMillis();
        System.out.println("Method 2 Time (ms): " + (endTime - startTime));
        measureMemoryUsage();

        // Method 3: Apache Commons IO
        startTime = System.currentTimeMillis();
        copyFileUsingApacheCommonsIO(path + "/voina-i-mir-3.pdf");
        endTime = System.currentTimeMillis();
        System.out.println("Method 3 Time (ms): " + (endTime - startTime));
        measureMemoryUsage();

        // Method 4: Files class (Java NIO)
        startTime = System.currentTimeMillis();
        copyFileUsingFilesClass(path + "/voina-i-mir-4.pdf");
        endTime = System.currentTimeMillis();
        System.out.println("Method 4 Time (ms): " + (endTime - startTime));
        measureMemoryUsage();
    }

    private static void copyFileUsingFileStreams(String destination) throws IOException {
        try (InputStream is = new FileInputStream(Task2.pdf.toFile());
             OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    private static void copyFileUsingFileChannel(String destination) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(Task2.pdf.toFile()).getChannel();
             FileChannel destChannel = new FileOutputStream(destination).getChannel()) {
            sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
        }
    }

    private static void copyFileUsingApacheCommonsIO(String destination) throws IOException {
        File sourceFile = new File(Task2.pdf.toUri());
        File destFile = new File(destination);
        FileUtils.copyFile(sourceFile, destFile);
    }

    private static void copyFileUsingFilesClass(String destination) throws IOException {
        Path sourcePath = Paths.get(Task2.pdf.toUri());
        Path destPath = Paths.get(destination);
        Files.copy(sourcePath, destPath);
    }

    private static void measureMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used Memory (bytes): " + usedMemory);
    }
}
