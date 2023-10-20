package ru.mirea.prac4.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import ru.mirea.prac4.common.Account;
import ru.mirea.prac4.common.Account2Stock;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.Stock;
import ru.mirea.prac4.common.util.JsonUtil;
import ru.mirea.prac4.server.repo.AccountRepository;
import ru.mirea.prac4.server.repo.MarketRequestRepository;
import ru.mirea.prac4.server.repo.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FireAndForgetControllerTest {

    @InjectMocks
    private FireAndForgetController fireAndForgetController;
    @Mock
    private MarketRequestRepository marketRequestRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createMarketRequestShouldSucceed() {
        MarketRequest marketRequest = new MarketRequest();
        var account = new Account(UUID.randomUUID(), "name", new ArrayList<>(), 10000.0);
        var stock = new Stock(UUID.randomUUID(), "Apple", "AAPL", 100.0, 10L);
        account.setAccount2Stocks(List.of(new Account2Stock(UUID.randomUUID(), account, stock, 20)));
        marketRequest.setAmount(10);
        marketRequest.setAccount(account);
        marketRequest.setStock(stock);

        Mockito.when(marketRequestRepository.save(marketRequest)).thenReturn(marketRequest);
        Mockito.when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.of(stock));
        Mockito.when(accountRepository.findByName("name")).thenReturn(Optional.of(account));

        fireAndForgetController.createBuyMarketRequest(JsonUtil.writeJson(marketRequest));
    }

    @Test
    public void processMarketRequestShouldHandleExceptions() {
        MarketRequest marketRequest = new MarketRequest();
        var account = new Account(UUID.randomUUID(), "name", new ArrayList<>(), 10000.0);
        var stock = new Stock(UUID.randomUUID(), "Apple", "AAPL", 100.0, 10L);
        account.setAccount2Stocks(List.of(new Account2Stock(UUID.randomUUID(), account, stock, 20)));
        marketRequest.setAmount(10);
        marketRequest.setAccount(account);
        marketRequest.setStock(stock);

        Mockito.when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.of(stock));
        Mockito.when(accountRepository.findByName("name")).thenReturn(Optional.of(account));
        Mockito.when(marketRequestRepository.save(marketRequest)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> fireAndForgetController.processMarketRequest(marketRequest));
    }

    @Test
    public void processMarketRequestShouldSucceed() {
        MarketRequest marketRequest = new MarketRequest();
        var stock = new Stock(UUID.randomUUID(), "Apple", "AAPL", 100.0, 100L);
        var account = new Account(UUID.randomUUID(), "name", new ArrayList<>(), 10000.0);
        account.setAccount2Stocks(List.of(new Account2Stock(UUID.randomUUID(), account, stock, 20)));
        marketRequest.setAmount(10);
        marketRequest.setStock(stock);
        marketRequest.setAccount(account);

        Mockito.when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.of(stock));
        Mockito.when(accountRepository.findByName("name")).thenReturn(Optional.of(account));

        fireAndForgetController.processMarketRequest(marketRequest);

        assertEquals(90, marketRequest.getStock().getAmount());
        assertEquals(9000.0, marketRequest.getAccount().getFunds());
        assertEquals(30, marketRequest.getAccount().getAccount2Stocks().get(0).getAmount());
    }
}
