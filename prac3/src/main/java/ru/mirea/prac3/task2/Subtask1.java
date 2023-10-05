package ru.mirea.prac3.task2;

import io.reactivex.rxjava3.core.Observable;

public class Subtask1 implements Runnable {

    @Override
    public void run() {
        Observable<Integer> randomNumbers = Observable
                .range(0, 1000)
                .map(i -> (int) Math.ceil(Math.random() * 1000));

        Observable<Integer> squares = randomNumbers
                .map(num -> num * num);

        squares.subscribe(System.out::println);
    }
}

