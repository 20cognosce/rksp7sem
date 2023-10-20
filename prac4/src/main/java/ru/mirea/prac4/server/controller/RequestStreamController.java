package ru.mirea.prac4.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import ru.mirea.prac4.common.util.JsonUtil;
import ru.mirea.prac4.server.repo.StockRepository;

import java.time.Duration;

@RequiredArgsConstructor
@Controller
public class RequestStreamController {

    private final StockRepository stockRepository;

    @MessageMapping("get-all-stocks")
    public Flux<String> getAllStocks() {
        System.out.println("[LOG] get-all-stocks received request");
        var stocks = stockRepository.findAll();

        return Flux.fromIterable(stocks)
                .map(JsonUtil::writeJson)
                .delayElements(Duration.ofSeconds(3));
    }
}
