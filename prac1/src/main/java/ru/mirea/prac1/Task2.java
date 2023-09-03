package ru.mirea.prac1;

import lombok.SneakyThrows;

import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.FutureTask;

public class Task2 {
    public void run() {
        new CustomInputHandler().handleInput();
    }

    private static class CustomInputHandler {
        private final Queue<FutureTask<Integer>> processingTasks = new ConcurrentLinkedQueue<>();
        private final Runnable outputTask = new OutputTask(processingTasks);

        public void handleInput() {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                try {
                    int request = Integer.parseInt(scanner.nextLine());
                    FutureTask<Integer> futureTask = new FutureTask<>(calculateSquareRoot(request));
                    processingTasks.add(futureTask);
                    new Thread(futureTask).start();
                    handleOutput();
                } catch (NumberFormatException e) {
                    System.out.println("Illegal input detected");
                }
            }
        }

        public void handleOutput() {
            var thread = new Thread(outputTask);
            thread.start();
        }

        private Callable<Integer> calculateSquareRoot(int value) {
            return () -> {
                Thread.sleep((Math.abs(new Random().nextInt() % 4000)) + 1000);
                return value * value;
            };
        }
    }

    private record OutputTask(Queue<FutureTask<Integer>> processingTasks) implements Runnable {
        @SneakyThrows
            @Override
            public void run() {
                while (!processingTasks.isEmpty()) {
                    var lastTask = processingTasks.remove();
                    System.out.println(lastTask.get());
                }
            }
        }
}
