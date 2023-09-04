package ru.mirea.prac1;

import lombok.SneakyThrows;
import ru.mirea.prac1.task3.ExtensionEnum;
import ru.mirea.prac1.task3.FileGenerator;
import ru.mirea.prac1.task3.FileHandler;
import ru.mirea.prac1.task3.FileQueue;
import ru.mirea.prac1.task3.handlers.JsonFileHandler;
import ru.mirea.prac1.task3.handlers.XlsFileHandler;
import ru.mirea.prac1.task3.handlers.XmlFileHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Task3 {
    public static final Path BASEDIR = Path.of(System.getProperty("user.dir") + "/src/main/resources/static");

    public static final Map<Integer, ExtensionEnum> mapExtension = Map.of(
            1, ExtensionEnum.JSON,
            2, ExtensionEnum.XML,
            3, ExtensionEnum.XLS);

    @SneakyThrows
    Task3() {
        if (!BASEDIR.toFile().exists()) {
            Files.createDirectory(BASEDIR);
        }
    }

    public void run() {
        FileQueue fileQueue = new FileQueue();
        FileGenerator fileGenerator = new FileGenerator(fileQueue);
        FileHandler fileHandlerJSON = new JsonFileHandler();
        FileHandler fileHandlerXML = new XmlFileHandler();
        FileHandler fileHandlerXLS = new XlsFileHandler();
        fileQueue.subscribe(fileHandlerJSON);
        fileQueue.subscribe(fileHandlerXML);
        fileQueue.subscribe(fileHandlerXLS);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Вводите два числа в одну строку через пробел.");
        System.out.println("""
                Первое число - тип файла:\s
                1. JSON\s
                2. XML\s
                3. XLS\s
                """);
        System.out.println("Второе число - размер файла (от 10 до 100 КБ)");

        boolean flag = true;
        while(scanner.hasNext()) {
            var line = scanner.nextLine().split(" ");

            if (flag) {
                System.out.println();
                flag = false;
            }

            var extensionNumber = Integer.parseInt(line[0]);
            var size = Integer.parseInt(line[1]);
            fileGenerator.generate(mapExtension.get(extensionNumber), size);
        }
    }
}
