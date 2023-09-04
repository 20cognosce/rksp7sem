package ru.mirea.prac1.task3;

import lombok.SneakyThrows;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class FileQueue {
    private final AtomicLong queueSize = new AtomicLong(0);
    private final List<FileHandler> subscribers = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Queue<CustomFile> jsonTopic = new ConcurrentLinkedQueue<>();
    private final Queue<CustomFile> xmlTopic = new ConcurrentLinkedQueue<>();
    private final Queue<CustomFile> xlsTopic = new ConcurrentLinkedQueue<>();

    public void publish(CustomFile file) {
        executorService.submit(getHandleEventTask(file));
    }

    public void subscribe(FileHandler handler) {
        subscribers.add(handler);
    }


    @SneakyThrows
    private Runnable getHandleEventTask(CustomFile file) {
        return () -> {
            long timestamp = System.currentTimeMillis();

            synchronized (queueSize) {
                while (queueSize.get() == 5) {
                    if (System.currentTimeMillis() - timestamp > 1000) {
                        System.out.printf("\nQueue is fulled (%s). [%s] is waiting...", queueSize.get(), Thread.currentThread().getId());
                        timestamp = System.currentTimeMillis();
                    }
                }

                System.out.printf("\nQueue is free [%s]. Publishing file %s", queueSize.get(), file.getFileName());
                switch (file.getExtension()) {
                    case JSON -> jsonTopic.add(file);
                    case XML -> xmlTopic.add(file);
                    case XLS -> xlsTopic.add(file);
                    default -> throw new IllegalArgumentException("Illegal extension");
                }
                queueSize.incrementAndGet();
                notifySubscribers(file.getExtension());
            }
        };
    }

    private void notifySubscribers(ExtensionEnum fileExtension) {
        subscribers.stream()
                .filter(handler -> handler.getAvailableFileExtension() == fileExtension)
                .forEach(handler -> {
                    switch (fileExtension) {
                        case JSON -> handler.handleFile(jsonTopic, queueSize);
                        case XML -> handler.handleFile(xmlTopic, queueSize);
                        case XLS -> handler.handleFile(xlsTopic, queueSize);
                        default -> throw new IllegalArgumentException("Illegal extension");
                    }
                });
    }
}
