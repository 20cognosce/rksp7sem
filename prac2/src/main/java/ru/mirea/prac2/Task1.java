package ru.mirea.prac2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Task1 {
    void run() {
        var filePath = Path.of(System.getProperty("user.dir") + "/src/main/resources/example.txt");
        var charset = StandardCharsets.UTF_8;

        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                String text = charset.decode(buffer).toString();
                System.out.print(text);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
