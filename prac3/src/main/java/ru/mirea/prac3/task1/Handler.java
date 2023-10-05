package ru.mirea.prac3.task1;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import ru.mirea.prac3.task1.common.Kind;
import ru.mirea.prac3.task1.common.Message;

public class Handler implements Observer<Message> {
    private Message lastLimitExceededTemperatureMessage;
    private Message lastLimitExceededCO2Message;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        System.out.println("Subscribed on " + d);
    }

    @Override
    public void onNext(@NonNull Message message) {
        System.out.println("Получено сообщение: " + message);

        if (message.kind() == Kind.TEMPERATURE) {
            if (message.value() > 25) {
                lastLimitExceededTemperatureMessage = message;
                System.out.println("Превышен показатель температуры: " + message.value());
            }
        }
        if (message.kind() == Kind.CO2) {
            if (message.value() > 70) {
                lastLimitExceededCO2Message = message;
                System.out.println("Превышен показатель CO2: " + message.value());
            }
        }
        if (lastLimitExceededTemperatureMessage != null && lastLimitExceededCO2Message != null) {
            if (lastLimitExceededTemperatureMessage.timestamp().getEpochSecond() == lastLimitExceededCO2Message.timestamp().getEpochSecond()) {
                System.out.println("!!! ALARM !!!");
                lastLimitExceededTemperatureMessage = null;
                lastLimitExceededCO2Message = null;
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {}

    @Override
    public void onComplete() {}
}
