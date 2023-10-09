package ru.mirea.prac4.client.controller;

import io.rsocket.util.DefaultPayload;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.mirea.prac4.common.Stock;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
public class ChannelController {
    private final RSocketRequester rSocketRequester;

    @GetMapping(value = "/stocks", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Publisher<Stock> provideChannelMarketRequests() {
        var data = Flux.just("one", "two")
                .map(DefaultPayload::create)
                .delayElements(Duration.ofMillis(1000));

        return rSocketRequester
                .route("channel-market-requests")
                .data(data)
                .retrieveFlux(Stock.class);
    }
}
