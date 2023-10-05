package ru.mirea.prac3.task1;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import ru.mirea.prac3.task1.common.Kind;
import ru.mirea.prac3.task1.common.Message;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Getter
public class SensorCO2 {
    private final Observable<Message> observable;

    public SensorCO2() {
        observable = Observable
                .interval(1, 1, TimeUnit.SECONDS)
                .map(tick -> new Message(
                        Kind.CO2,
                        new Random().nextInt(30, 100 + 1),
                        Instant.now()));
    }
}
