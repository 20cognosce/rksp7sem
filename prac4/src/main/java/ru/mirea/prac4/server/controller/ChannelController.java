package ru.mirea.prac4.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.rsocket.util.DefaultPayload;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.server.repo.AccountRepository;
import ru.mirea.prac4.server.repo.MarketRequestRepository;
import ru.mirea.prac4.server.repo.StockRepository;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class ChannelController {

    private final MarketRequestRepository marketRequestRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    @MessageMapping("sell-market-request")
    public Flux<String> createSellMarketRequest(Publisher<MarketRequest> marketRequests) {
        return Flux.from(marketRequests)
                .doOnNext(marketRequest -> {
                    System.out.println("[LOG] sell-market-request received: " + marketRequest);
                })
                .map(this::processMarketRequest);
    }

    @SneakyThrows
    @Transactional
    public String processMarketRequest(MarketRequest marketRequest) {
        var ticker = marketRequest.getStock().getTicker();
        var accountName = marketRequest.getAccount().getName();

        var stock = stockRepository.findByTicker(ticker)
                .orElseThrow(() -> new EntityNotFoundException("Stock with ticker " + ticker + " not found"));
        var account = accountRepository.findByName(accountName)
                .orElseThrow(() -> new EntityNotFoundException("Account with uuid " + accountName + " not found"));
        var account2stock = account.getAccount2Stocks().stream()
                .filter(account2Stock -> ticker.equals(account2Stock.getStock().getTicker()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    if (list.size() != 1) {
                        throw new IllegalArgumentException("Database inconsistency error");
                    }
                    return list.get(0);
                }));

        marketRequest.setAccount(account);
        marketRequest.setStock(stock);

        stock.setAmount(stock.getAmount() + marketRequest.getAmount());
        account2stock.setAmount(account2stock.getAmount() - marketRequest.getAmount());
        account.setFunds(account.getFunds() + marketRequest.getAmount() * stock.getPrice());

        stockRepository.save(stock);
        accountRepository.save(account);
        marketRequestRepository.save(marketRequest);

        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(
                Map.of("Stocks sold", marketRequest.getAmount(),
                        "Ticker", marketRequest.getStock().getTicker(),
                        "Total price", marketRequest.getAmount() * stock.getPrice())
        );
    }
}
