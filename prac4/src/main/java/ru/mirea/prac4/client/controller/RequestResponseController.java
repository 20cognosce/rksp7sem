package ru.mirea.prac4.client.controller;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;

    @GetMapping(value = "/account/{name}")
    public Publisher<String> getAccountByName(@PathVariable("name") String name) {
        return rSocketRequester
                .route("get-account-by-name")
                .data(name)
                .retrieveMono(String.class);
    }
}
