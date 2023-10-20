package ru.mirea.prac4.client.controller;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.prac4.common.Stock;
import ru.mirea.prac4.common.util.JsonUtil;

@RequiredArgsConstructor
@RestController
public class RequestStreamController {
    private final RSocketRequester rSocketRequester;

    @GetMapping(value = "/stocks", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Publisher<Stock> getAllStocks() {
        return rSocketRequester
                .route("get-all-stocks")
                .retrieveFlux(String.class)
                .map(this::deserialize);
    }

    private Stock deserialize(String json) {
        return JsonUtil.readJson(json, Stock.class);
    }
}
