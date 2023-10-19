package ru.mirea.prac4.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EntityScan("ru.mirea.prac4.common")
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .main(ServerApplication.class)
                .sources(ServerApplication.class)
                .profiles("server")
                .run(args);
    }
}
