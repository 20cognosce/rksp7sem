package ru.mirea.prac1;

import lombok.SneakyThrows;
import ru.mirea.prac1.task3.FileGenerator;
import ru.mirea.prac1.task3.FileHandler;
import ru.mirea.prac1.task3.FileQueue;
import ru.mirea.prac1.task3.JsonFileHandler;
import ru.mirea.prac1.task3.XlsFileHandler;
import ru.mirea.prac1.task3.XmlFileHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Task3 {
    public static final Path BASEDIR = Path.of(System.getProperty("user.dir") + "/src/main/resources/static");

    public static final Map<Integer, String> mapExtension = Map.of(
            1, "JSON",
            2, "XML",
            3, "XLS");

    @SneakyThrows
    Task3() {
        if (!BASEDIR.toFile().exists()) {
            Files.createDirectory(BASEDIR);
        }
    }

    public void run() {
        FileQueue fileQueue = new FileQueue();
        FileGenerator fileGenerator = new FileGenerator(fileQueue);
        FileHandler fileHandlerJSON = new JsonFileHandler(fileQueue);
        FileHandler fileHandlerXML = new XmlFileHandler(fileQueue);
        FileHandler fileHandlerXLS = new XlsFileHandler(fileQueue);

        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                Тип файла:\s
                1. JSON\s
                2. XML\s
                3. XLS\s
                """);

        var extensionNumber = Integer.parseInt(scanner.nextLine());
        System.out.println("Размер файла (от 10 до 100 КБ)");
        var size = Integer.parseInt(scanner.nextLine());

        fileGenerator.generate(mapExtension.get(extensionNumber), size);
    }
}
