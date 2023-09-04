package ru.mirea.prac1.task3;

import lombok.SneakyThrows;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public abstract class FileHandler {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    protected abstract ExtensionEnum getAvailableFileExtension();

    protected void handleFile(Queue<CustomFile> topic, AtomicLong queueSize) {
        while (!topic.isEmpty()) {
            var file = topic.poll();
            executorService.submit(getHandleFileTask(file, queueSize));
        }
    }

    protected Runnable getHandleFileTask(CustomFile file, AtomicLong queueSize) {
        return () -> {
            this.sleep(100 * file.getSizeKB());
            System.out.printf("\n=== Handler %s processed %s ===", getAvailableFileExtension(), file.getFileName());
            queueSize.decrementAndGet();
        };
    }

    @SneakyThrows
    private void sleep(int millis) {
        Thread.sleep(millis);
    }
}
