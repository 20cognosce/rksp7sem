package ru.mirea.prac4.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import ru.mirea.prac4.server.repo.StockRepository;

import java.time.Duration;

@RequiredArgsConstructor
@Controller
public class RequestStreamController {

    private final StockRepository stockRepository;
    private final ObjectWriter jsonWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @MessageMapping("get-all-stocks")
    public Flux<String> getAllStocks() {
        System.out.println("[LOG] get-all-stocks received request");
        var stocks = stockRepository.findAll();

        return Flux.fromIterable(stocks)
                .map(this::serialize)
                .delayElements(Duration.ofSeconds(3));
    }

    @SneakyThrows
    private String serialize(Object object) {
        return jsonWriter.writeValueAsString(object);
    }
}
