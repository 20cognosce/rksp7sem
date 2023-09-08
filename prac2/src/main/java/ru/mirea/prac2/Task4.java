package ru.mirea.prac2;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Task4 {
    String directoryPath = System.getProperty("user.dir") + "/src/main/resources/static";
    Map<Path, AbstractMap.SimpleEntry<Long, Short>> fileSizeHashSumMap = new HashMap<>();

    @SneakyThrows
    void run() {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path directory = Paths.get(directoryPath);
        directory.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        WatchKey key = watchService.take();
        Path dir = (Path) key.watchable();

        while (true) {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path fileName = (Path) event.context();
                Path filePath = dir.resolve(fileName);

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("File created: " + fileName);
                    detectFileChanges(filePath);
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("File modified: " + fileName);
                    detectFileChanges(filePath);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    //FILE IS ALREADY DELETED IN FILE SYSTEM
                    System.out.println("File deleted: " + fileName);
                    displayFileInformation(filePath);
                }
            }
        }
    }

    @SneakyThrows
    private void detectFileChanges(Path filePath) {
        var file = new File(filePath.toUri());
        fileSizeHashSumMap.put(filePath, new AbstractMap.SimpleEntry<>(file.length(), Task3.calculateChecksum(file.getPath())));
    }

    private void displayFileInformation(Path filePath) {
        System.out.println("Attributes of deleted file [bytes size, hash-sum]:");
        System.out.println(fileSizeHashSumMap.remove(filePath));
    }
}



