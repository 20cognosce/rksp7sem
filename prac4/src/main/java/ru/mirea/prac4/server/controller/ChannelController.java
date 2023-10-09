package ru.mirea.prac4.server.controller;

import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.Stock;

@Controller
public class ChannelController {

    @MessageMapping("channel-market-requests")
    public Flux<Stock> getStocks(Publisher<MarketRequest> payloads) {

        return Flux.from(payloads)
                .flatMap(payload -> Flux.fromStream(
                        payload.getDataUtf8().codePoints()
                                        .mapToObj(c -> String.valueOf((char) c))
                                        .map(i -> DefaultPayload.create("channel: " + i))))
                .doOnNext(System.out::println);
    }
}
