package ru.mirea.prac4.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ClientApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .main(ClientApplication.class)
                .sources(ClientApplication.class)
                .profiles("client")
                .run(args);
    }
}
