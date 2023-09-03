package ru.mirea.prac1.task3;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class FileQueue {
    private List<FileHandler> listeners;
    private final AtomicLong queueSize = new AtomicLong(0);
    private final Queue<CustomFile> jsonTopic = new ConcurrentLinkedQueue<>();
    private final Queue<CustomFile> xmlTopic = new ConcurrentLinkedQueue<>();
    private final Queue<CustomFile> xlsTopic = new ConcurrentLinkedQueue<>();

    public void produce(CustomFile file) {
        new Thread(getHandleEventTask(file)).start();
    }

    public void subsribe(FileHandler handler) {
        listeners.add(handler);
    }

    public Runnable getHandleEventTask(CustomFile file) {
        return () -> {
            synchronized (queueSize) {
                while (queueSize.get() > 5) {

                }

                switch (file.getExtension()) {
                    case "JSON" -> jsonTopic.add(file);
                    case "XML" -> xmlTopic.add(file);
                    case "XLS" -> xlsTopic.add(file);
                    default -> throw new IllegalArgumentException("Illegal extension");
                }
                queueSize.incrementAndGet();
            }
        };
    }

}
