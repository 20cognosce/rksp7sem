package ru.mirea.prac3.task4;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.SneakyThrows;
import ru.mirea.prac3.task4.common.CustomFile;
import ru.mirea.prac3.task4.common.ExtensionEnum;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public abstract class FileHandler implements Observer<CustomFile> {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final AtomicLong queueSize;

    protected FileHandler(AtomicLong queueSize) {
        this.queueSize = queueSize;
    }

    protected abstract ExtensionEnum getAvailableFileExtension();

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        System.out.println(getAvailableFileExtension() + " Handler subscribed on " + d);
    }

    @Override
    public void onNext(@NonNull CustomFile file) {
        executorService.submit(getHandleFileTask(file));
    }

    @Override
    public void onError(@NonNull Throwable e) {}

    @Override
    public void onComplete() {}

    protected Runnable getHandleFileTask(CustomFile file) {
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
