package ru.mirea.prac2;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Task3 {

    void run() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/example.txt";

        try {
            short checksum = calculateChecksum(filePath);
            System.out.println("Checksum: " + checksum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static short calculateChecksum(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             FileChannel fileChannel = fileInputStream.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(2); // 2 bytes for a short

            short checksum = 0; //2^16

            while (fileChannel.read(buffer) != -1) {
                buffer.flip(); // Switch to read mode
                while (buffer.hasRemaining()) {
                    checksum ^= buffer.get(); // XOR operation
                    // 001 XOR 011 = 010, i.e. summing diffs of every two nearby chars
                }
                buffer.clear(); // Switch back to write mode
            }

            return checksum;
        }
    }
}
