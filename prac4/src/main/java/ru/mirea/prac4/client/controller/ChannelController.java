package ru.mirea.prac4.client.controller;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.mirea.prac4.common.Account;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.Stock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ChannelController {
    private final RSocketRequester rSocketRequester;

    @PostMapping(value = "/sell-market-request", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Publisher<String> createSellMarketRequest() {
        var data = Flux.just("SBER", "YNDX", "GAZP")
                .map(ticker -> {
                    var account = new Account();
                    account.setName("one");

                    var stock = new Stock();
                    stock.setTicker(ticker);

                    return new MarketRequest(UUID.randomUUID(), account, stock, 1, LocalDateTime.now());
                })
                .delayElements(Duration.ofMillis(1000));

        return rSocketRequester
                .route("sell-market-requests")
                .data(data)
                .retrieveFlux(String.class);
    }
}
