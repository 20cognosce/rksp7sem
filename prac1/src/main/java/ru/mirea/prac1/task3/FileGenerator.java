package ru.mirea.prac1.task3;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class FileGenerator {
    private final AtomicLong idCounter = new AtomicLong(0);
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final FileQueue fileQueue;

    public FileGenerator(FileQueue fileQueue) {
        this.fileQueue = fileQueue;
    }

    @SneakyThrows
    public void generate(ExtensionEnum extension, Integer sizeKB) {
        if (sizeKB < 10 || sizeKB > 100) {
            System.out.println("Illegal file size");
            return;
        }

        executorService.submit(getGenerateFileTask(extension, sizeKB));
    }

    private Runnable getGenerateFileTask(ExtensionEnum extension, Integer sizeKB) {
        return () -> fileQueue.publish(
                new CustomFile("file" + idCounter.incrementAndGet(), extension, sizeKB)
        );
    }
}
