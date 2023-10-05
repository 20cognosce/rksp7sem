package ru.mirea.prac3.task2;

import io.reactivex.rxjava3.core.Observable;

import java.util.Random;

public class Subtask3 implements Runnable {

    @Override
    public void run() {
        Observable<?> randomNumbers = Observable
                .generate(emitter -> emitter.onNext(getRandomNumber()))
                .take(10)
                .skip(3);

        randomNumbers.subscribe(System.out::println);
    }

    private static int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(10);
    }
}
