package ru.mirea.prac3.task2;

import io.reactivex.rxjava3.core.Observable;

import java.util.Random;

public class Subtask2 implements Runnable {

    @Override
    public void run() {
        Observable<?> letters = Observable
                .generate(emitter -> emitter.onNext(getRandomLetter()))
                .take(1000);

        Observable<?> numbers = Observable
                .generate(emitter -> emitter.onNext(getRandomNumber()))
                .take(1000);

        Observable<String> combined = Observable.zip(letters, numbers, (letter, number) -> (String) letter + number);

        combined.subscribe(System.out::println);
    }

    private static String getRandomLetter() {
        Random random = new Random();
        char letter = (char) (random.nextInt(26) + 'A');
        return Character.toString(letter);
    }

    private static int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(10);
    }
}
