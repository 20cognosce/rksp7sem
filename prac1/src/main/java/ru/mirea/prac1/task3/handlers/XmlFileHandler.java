package ru.mirea.prac1.task3.handlers;

import ru.mirea.prac1.task3.ExtensionEnum;
import ru.mirea.prac1.task3.FileHandler;

import static ru.mirea.prac1.task3.ExtensionEnum.XML;

public class XmlFileHandler extends FileHandler {

    @Override
    public ExtensionEnum getAvailableFileExtension() {
        return XML;
    }
}
