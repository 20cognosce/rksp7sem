package ru.mirea.prac3.task1;

import lombok.SneakyThrows;

public class Task1Manager implements Runnable {

    @SneakyThrows
    @Override
    public void run() {
        var sensorTemperature = new SensorTemperature();
        var sensorCO2 = new SensorCO2();
        var handler = new Handler();

        sensorTemperature.getObservable().subscribe(handler);
        sensorCO2.getObservable().subscribe(handler);

        Thread.sleep(100_000L);
    }
}
