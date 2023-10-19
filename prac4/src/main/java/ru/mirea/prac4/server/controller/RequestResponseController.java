package ru.mirea.prac4.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.mirea.prac4.server.repo.AccountRepository;

@RequiredArgsConstructor
@Controller
public class RequestResponseController {
    private final AccountRepository accountRepository;

    @SneakyThrows
    @MessageMapping("get-account-by-name")
    public Mono<String> getAccountByName(String name) {
        System.out.println("[LOG] get-account-by-name received: " + name);
        ObjectWriter jsonWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var account = accountRepository.findByName(name);

        return Mono.just(account.isPresent() ?
                jsonWriter.writeValueAsString(account.get()) :
                jsonWriter.writeValueAsString(null));
    }
}