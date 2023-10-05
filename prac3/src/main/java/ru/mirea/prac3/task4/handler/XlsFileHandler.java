package ru.mirea.prac3.task4.handler;

import ru.mirea.prac3.task4.common.ExtensionEnum;
import ru.mirea.prac3.task4.FileHandler;

import java.util.concurrent.atomic.AtomicLong;

import static ru.mirea.prac3.task4.common.ExtensionEnum.XLS;

public class XlsFileHandler extends FileHandler {

    public XlsFileHandler(AtomicLong queueSize) {
        super(queueSize);
    }

    @Override
    public ExtensionEnum getAvailableFileExtension() {
        return XLS;
    }
}
