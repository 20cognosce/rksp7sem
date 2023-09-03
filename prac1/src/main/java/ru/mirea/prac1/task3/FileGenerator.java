package ru.mirea.prac1.task3;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicLong;

public class FileGenerator {
    private final AtomicLong idCounter = new AtomicLong(0);
    private final FileQueue fileQueue;

    public FileGenerator(FileQueue fileQueue) {
        this.fileQueue = fileQueue;
    }

    @SneakyThrows
    public void generate(String extension, Integer sizeKB) {
        if (sizeKB < 10 || sizeKB > 100) {
            System.out.println("Illegal file size");
            return;
        }

        CustomFile file;
        switch (extension) {
            case "JSON" -> file = new CustomFile("file" + idCounter.incrementAndGet() + ".json", sizeKB);
            case "XML" -> file = new CustomFile("file" + idCounter.incrementAndGet() + ".xml", sizeKB);
            case "XLS" -> file = new CustomFile("file" + idCounter.incrementAndGet() + ".xls", sizeKB);
            default -> throw new IllegalArgumentException("Illegal extension");
        }

        fileQueue.produce(file);
    }
}
