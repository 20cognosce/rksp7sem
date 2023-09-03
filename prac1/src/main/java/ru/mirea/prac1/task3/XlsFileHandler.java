package ru.mirea.prac1.task3;

public class XlsFileHandler implements FileHandler {
    private final FileQueue fileQueue;

    public XlsFileHandler(FileQueue fileQueue) {
        this.fileQueue = fileQueue;
    }

}
