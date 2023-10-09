package ru.mirea.prac4.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.mirea.prac4.common.Stock;
import ru.mirea.prac4.server.repo.StockRepository;

@RequiredArgsConstructor
@Controller
public class RequestResponseController {
    private final StockRepository stockRepository;

    @MessageMapping("stock-by-ticker")
    public Mono<Stock> getStockByTicker(String ticker) {
        return Mono.just(stockRepository.findByTicker(ticker)
                .orElse(new Stock())
        );
    }
}
