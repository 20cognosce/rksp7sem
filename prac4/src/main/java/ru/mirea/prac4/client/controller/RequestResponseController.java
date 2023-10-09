package ru.mirea.prac4.client.controller;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.prac4.common.Stock;

@RequiredArgsConstructor
@RestController
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;

    @GetMapping(value = "/stocks/{ticker}")
    public Publisher<Stock> getStockByTicker(@PathVariable("ticker") String ticker) {
        return rSocketRequester
                .route("stock-by-ticker")
                .data(new MarketDataRequest(stock))
                .retrieveMono(Stock.class);
    }
}
