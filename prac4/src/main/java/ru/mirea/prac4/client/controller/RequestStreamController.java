package ru.mirea.prac4.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.prac4.common.Stock;

@RequiredArgsConstructor
@RestController
public class RequestStreamController {
    private final RSocketRequester rSocketRequester;
    private final ObjectReader jsonReader =  new ObjectMapper().reader();

    @GetMapping(value = "/stocks", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Publisher<Stock> getAllStocks() {
        return rSocketRequester
                .route("get-all-stocks")
                .retrieveFlux(String.class)
                .map(this::deserialize);
    }

    @SneakyThrows
    private Stock deserialize(String json) {
        return jsonReader.readValue(json, Stock.class);
    }
}
