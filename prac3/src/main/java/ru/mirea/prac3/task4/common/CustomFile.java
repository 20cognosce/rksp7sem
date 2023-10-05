package ru.mirea.prac3.task4.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import static ru.mirea.prac3.task4.Task4Manager.BASEDIR;

@Getter
public class CustomFile {
    private final Path path;
    private final String fileName;
    private final ExtensionEnum extension;
    private final Integer sizeKB;

    @SneakyThrows
    public CustomFile(@NonNull String fileName, @NonNull ExtensionEnum extension, Integer sizeKB) {
        this.path = Path.of(BASEDIR + "/" + fileName + extension.toFileExtension());
        this.fileName = fileName + extension.toFileExtension();
        this.extension = extension;
        this.sizeKB = sizeKB;

        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
            System.out.printf("\nCreating file %s in file system...", this.getFileName());
            Thread.sleep((Math.abs(new Random().nextInt() % 900)) + 100);
            out.write(new byte[1024 * sizeKB]);
        }
    }
}
