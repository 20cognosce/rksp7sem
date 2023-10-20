package ru.mirea.prac4.client.controller;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.mirea.prac4.common.Account;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.Stock;
import ru.mirea.prac4.common.dto.SellMarketRequestDto;
import ru.mirea.prac4.common.dto.TickerListDto;
import ru.mirea.prac4.common.util.JsonUtil;

import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ChannelController {
    private final RSocketRequester rSocketRequester;

    @PostMapping(value = "/sell-market-requests", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Publisher<SellMarketRequestDto> createSellMarketRequest(@RequestBody TickerListDto tickerListDto) {
        var data = Flux.fromIterable(tickerListDto.getTickers())
                .map(ticker -> {
                    var account = new Account();
                    account.setName("one");

                    var stock = new Stock();
                    stock.setTicker(ticker);

                    return JsonUtil.writeJson(new MarketRequest(UUID.randomUUID(), account, stock, 1, null));
                })
                .delayElements(Duration.ofSeconds(3));

        return rSocketRequester
                .route("sell-market-requests")
                .data(data)
                .retrieveFlux(String.class)
                .map(e -> JsonUtil.readJson(e, SellMarketRequestDto.class));
    }
}
