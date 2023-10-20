package ru.mirea.prac4.server.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.dto.SellMarketRequestDto;
import ru.mirea.prac4.common.util.JsonUtil;
import ru.mirea.prac4.server.repo.AccountRepository;
import ru.mirea.prac4.server.repo.MarketRequestRepository;
import ru.mirea.prac4.server.repo.StockRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class ChannelController {

    private final MarketRequestRepository marketRequestRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    @MessageMapping("sell-market-requests")
    public Flux<String> createSellMarketRequest(Publisher<String> marketRequests) {
        return Flux.from(marketRequests)
                .doOnNext(marketRequest -> System.out.println("[LOG] sell-market-request received: " + marketRequest))
                .map(e -> JsonUtil.readJson(e, MarketRequest.class))
                .map(this::processMarketRequest)
                .delayElements(Duration.ofSeconds(3));
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
        var account2stockList = account.getAccount2Stocks().stream()
                .filter(account2Stock -> ticker.equals(account2Stock.getStock().getTicker()))
                .toList();

        if (account2stockList.size() > 1) {
            throw new IllegalArgumentException("Database inconsistency error");
        }
        if (account2stockList.size() == 0) {
            return JsonUtil.writeJson(new SellMarketRequestDto(0, marketRequest.getStock().getTicker(), 0d));
        }

        var account2stock = account2stockList.get(0);

        marketRequest.setAccount(account);
        marketRequest.setStock(stock);
        marketRequest.setDateTime(LocalDateTime.now());
        stock.setAmount(stock.getAmount() + marketRequest.getAmount());
        account2stock.setAmount(account2stock.getAmount() - marketRequest.getAmount());
        account.setFunds(account.getFunds() + marketRequest.getAmount() * stock.getPrice());

        stockRepository.save(stock);
        accountRepository.save(account);
        marketRequestRepository.save(marketRequest);

        return JsonUtil.writeJson(new SellMarketRequestDto(
                marketRequest.getAmount(),
                marketRequest.getStock().getTicker(),
                marketRequest.getAmount() * stock.getPrice())
        );
    }
}
