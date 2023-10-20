package ru.mirea.prac4.client.controller;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.util.JsonUtil;

@RequiredArgsConstructor
@RestController
public class FireAndForgetController {
    private final RSocketRequester rSocketRequester;

    @PostMapping(value = "/buy-market-request")
    public Publisher<Void> createBuyMarketRequest(@RequestBody MarketRequest marketRequest) {
        return rSocketRequester
                .route("buy-market-request")
                .data(JsonUtil.writeJson(marketRequest))
                .send();
    }
}
