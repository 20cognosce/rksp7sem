package ru.mirea.prac3.task4.handler;

import ru.mirea.prac3.task4.FileHandler;
import ru.mirea.prac3.task4.common.ExtensionEnum;

import java.util.concurrent.atomic.AtomicLong;

import static ru.mirea.prac3.task4.common.ExtensionEnum.JSON;

public class JsonFileHandler extends FileHandler {

    public JsonFileHandler(AtomicLong queueSize) {
        super(queueSize);
    }

    @Override
    public ExtensionEnum getAvailableFileExtension() {
        return JSON;
    }
}
