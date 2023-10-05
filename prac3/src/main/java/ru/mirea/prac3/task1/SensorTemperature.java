package ru.mirea.prac3.task1;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import ru.mirea.prac3.task1.common.Kind;
import ru.mirea.prac3.task1.common.Message;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Getter
public class SensorTemperature {
    private final Observable<Message> observable;

    public SensorTemperature() {
        observable = Observable
                .interval(1, 1, TimeUnit.SECONDS)
                .map(tick -> new Message(
                        Kind.TEMPERATURE,
                        new Random().nextInt(15, 30 + 1),
                        Instant.now()));
    }
}
