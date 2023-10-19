package ru.mirea.prac4.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.prac4.common.MarketRequest;

@RequiredArgsConstructor
@RestController
public class FireAndForgetController {
    private final RSocketRequester rSocketRequester;
    private final ObjectWriter jsonWriter =  new ObjectMapper().writer();

    @PostMapping(value = "/buy-market-request")
    public Publisher<Void> createBuyMarketRequest(@RequestBody MarketRequest marketRequest) {
        return rSocketRequester
                .route("buy-market-request")
                .data(serialize(marketRequest))
                .send();
    }

    @SneakyThrows
    private String serialize(Object object) {
        return jsonWriter.writeValueAsString(object);
    }
}
