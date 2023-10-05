package ru.mirea.prac3.task4;

import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.mirea.prac3.task4.common.CustomFile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class FileQueue {
    private final AtomicLong queueSize = new AtomicLong(0);
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final PublishSubject<CustomFile> jsonTopic = PublishSubject.create();
    private final PublishSubject<CustomFile> xmlTopic = PublishSubject.create();
    private final PublishSubject<CustomFile> xlsTopic = PublishSubject.create();

    public void publish(CustomFile file) {
        executorService.submit(getHandleEventTask(file));
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

                queueSize.incrementAndGet();
                System.out.printf("\nQueue is free [%s]. Publishing file %s", queueSize.get(), file.getFileName());
                switch (file.getExtension()) {
                    case JSON -> jsonTopic.onNext(file);
                    case XML -> xmlTopic.onNext(file);
                    case XLS -> xlsTopic.onNext(file);
                    default -> throw new IllegalArgumentException("Illegal extension");
                }
            }
        };
    }
}
