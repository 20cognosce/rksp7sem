package ru.mirea.prac4.server.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.mirea.prac4.common.Account2Stock;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.util.JsonUtil;
import ru.mirea.prac4.server.repo.AccountRepository;
import ru.mirea.prac4.server.repo.MarketRequestRepository;
import ru.mirea.prac4.server.repo.StockRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class  FireAndForgetController {
    private final MarketRequestRepository marketRequestRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    @MessageMapping("buy-market-request")
    public void createBuyMarketRequest(String marketRequest) {
        System.out.println("[LOG] buy-market-request received: " + marketRequest);
        MarketRequest deserialized = JsonUtil.readJson(marketRequest, MarketRequest.class);
        processMarketRequest(deserialized);
    }

    //Конечно здесь нужно сохранить заявку и в фоновом режиме пытаться её выполнить, но не хочу усложнять
    @Transactional
    public void processMarketRequest(MarketRequest marketRequest) {
        var ticker = marketRequest.getStock().getTicker();
        var accountName = marketRequest.getAccount().getName();

        var stock = stockRepository.findByTicker(ticker)
                .orElseThrow(() -> new EntityNotFoundException("Stock with ticker " + ticker + " not found"));
        var account = accountRepository.findByName(accountName)
                .orElseThrow(() -> new EntityNotFoundException("Account with name " + accountName + " not found"));
        var account2stock = account.getAccount2Stocks().stream()
                .filter(account2Stock -> ticker.equals(account2Stock.getStock().getTicker()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    if (list.size() > 1) {
                        throw new IllegalArgumentException("Database inconsistency error");
                    }
                    if (list.size() == 0) {
                        account.setAccount2Stocks(List.of(new Account2Stock(UUID.randomUUID(), account, stock, 0)));
                        return account.getAccount2Stocks().get(0);
                    }
                    return list.get(0);
                }));

        if (stock.getAmount() < marketRequest.getAmount()) {
            throw new IllegalArgumentException("Stocks are sold out");
        }
        if (account.getFunds() < marketRequest.getAmount() * stock.getPrice()) {
            throw new IllegalArgumentException("Not enough money");
        }

        stock.setAmount(stock.getAmount() - marketRequest.getAmount());
        account2stock.setAmount(account2stock.getAmount() + marketRequest.getAmount());
        account.setFunds(account.getFunds() - marketRequest.getAmount() * stock.getPrice());

        marketRequest.setAccount(account);
        marketRequest.setStock(stock);
        marketRequest.setDateTime(LocalDateTime.now());

        stockRepository.save(stock);
        accountRepository.save(account);
        marketRequestRepository.save(marketRequest);
    }
}
