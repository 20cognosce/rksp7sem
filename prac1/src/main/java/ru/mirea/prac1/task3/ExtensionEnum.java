package ru.mirea.prac1.task3;

public enum ExtensionEnum {

    JSON,
    XML,
    XLS;

    public String toFileExtension() {
        switch (this) {
            case JSON -> {
                return ".json";
            }
            case XML -> {
                return ".xml";
            }
            case XLS -> {
                return ".xls";
            }
            default -> throw new IllegalArgumentException("Illegal extension");
        }
    }
}
