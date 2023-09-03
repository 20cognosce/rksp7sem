package ru.mirea.prac1.task3;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import static ru.mirea.prac1.Task3.BASEDIR;

@Getter
public class CustomFile {
    private final Path path;
    private final String fileName;
    private final String extension;
    private final Integer sizeKB;

    @SneakyThrows
    public CustomFile(@NonNull String fileName, Integer sizeKB) {
        this.path = Path.of(BASEDIR + "/" + fileName);
        this.fileName = fileName;
        this.extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        this.sizeKB = sizeKB;

        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
            Thread.sleep((Math.abs(new Random().nextInt() % 900)) + 100);
            out.write(new byte[1024 * sizeKB]);
        }
    }
}
