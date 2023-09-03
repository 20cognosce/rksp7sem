package ru.mirea.prac1;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class Task1 {
    private final static int[] arr;
    private final Map<String, Double> results = new ConcurrentHashMap<>();

    public void benchmark() {
        System.out.println("##### TASK 1 BENCHMARK #####");

        results.put("SequentialRun", measureExecution(new SequentialRun()));
        results.put("ParallelFutureRun", measureExecution(new ParallelFutureRun()));
        results.put("ParallelForkJoinRun", measureExecution(new ParallelForkJoinRun()));

        results.forEach((key, value) -> System.out.println("[" + key + " " + value + " millis]"));
    }

    static {
        arr = IntStream.generate(() -> new Random().nextInt(1_000))
                .limit(1_000)
                .toArray();
    }

    private static class SequentialRun implements Callable<Integer> {
        @SneakyThrows
        @Override
        public Integer call() {
            int sum = 0;
            for (int e : arr) {
                Thread.sleep(1);
                sum += e;
            }
            return sum;
        }
    }

    private static class ParallelFutureRun implements Callable<Integer> {
        int numThreads = 10;
        int chunkSize = arr.length / numThreads;
        int startIndex = 0;
        int endIndex = chunkSize;
        List<Future<Integer>> results = new ArrayList<>();

        @SneakyThrows
        @Override
        public Integer call() {
            for (int i = 0; i < numThreads; i++) {
                final int start = startIndex;
                final int end = endIndex;

                FutureTask<Integer> futureTask = new FutureTask<>(getCallable(start, end));
                results.add(futureTask);
                new Thread(futureTask).start();

                startIndex = endIndex;
                endIndex = endIndex + chunkSize;
            }

            return countSum(results);
        }

        protected Callable<Integer> getCallable(int start, int end) {
            return () -> {
                int sum = 0;
                for (int i = start; i < end; i++) {
                    Thread.sleep(1);
                    sum += arr[i];
                }
                return sum;
            };
        }

        @SneakyThrows
        protected int countSum(List<Future<Integer>> sums) {
            int sum = 0;
            for (Future<Integer> result : sums) {
                sum += result.get();
            }
            return sum;
        }
    }

    private static class ParallelForkJoinRun implements Callable<Integer> {
        List<ArraySumTask> dividedTasks = new ArrayList<>();

        ParallelForkJoinRun() {
            for (int i = 0; i < 200; i++) {
                dividedTasks.add(new ArraySumTask(i * 5, (i + 1) * 5));
            }
        }

        @SneakyThrows
        @Override
        public Integer call() {
            return ForkJoinTask.invokeAll(dividedTasks)
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        }

        static class ArraySumTask extends RecursiveTask<Integer> {
            private final int startIndex;
            private final int endIndex;

            ArraySumTask(int startIndex, int endIndex) {
                this.startIndex = startIndex;
                this.endIndex = endIndex;
            }

            @SneakyThrows
            @Override
            protected Integer compute() {
                int sum = 0;
                for (int i = startIndex; i < endIndex; i++) {
                    Thread.sleep(1);
                    sum += arr[i];
                }
                return sum;
            }
        }
    }

    @SneakyThrows
    private Double measureExecution(Callable<Integer> runnable) {
        Integer result;

        long start = System.nanoTime();
        result = runnable.call();
        double millis = (System.nanoTime() - start) * 1.0 / 1_000_000; //millis

        System.out.println("sum = " + result);
        return millis;
    }
}
