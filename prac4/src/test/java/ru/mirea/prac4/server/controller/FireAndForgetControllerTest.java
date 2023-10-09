package ru.mirea.prac4.server.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import ru.mirea.prac4.common.Account;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.Stock;
import ru.mirea.prac4.server.repo.AccountRepository;
import ru.mirea.prac4.server.repo.MarketRequestRepository;
import ru.mirea.prac4.server.repo.StockRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        marketRequest.setStock(new Stock(UUID.randomUUID(), "Apple", "AAPL", 100.0, 10L));
        marketRequest.setAccount(new Account(UUID.randomUUID(), new ArrayList<>(), 10000.0));

        Mockito.when(marketRequestRepository.save(marketRequest)).thenReturn(marketRequest);
        Mono<Void> result = fireAndForgetController.createMarketRequest(marketRequest);
    }

    @Test
    public void createMarketRequestShouldHandleExceptions() {
        MarketRequest marketRequest = new MarketRequest();
        Mockito.when(marketRequestRepository.save(marketRequest)).thenThrow(new RuntimeException("Test Exception"));
        assertThrows(RuntimeException.class, () -> fireAndForgetController.createMarketRequest(marketRequest));
    }

    @Test
    public void processMarketRequestShouldSucceed() {
        MarketRequest marketRequest = new MarketRequest();
        var stock = new Stock(UUID.randomUUID(), "Apple", "AAPL", 100.0, 10L);
        var account = new Account(UUID.fromString("test_uuid"), new ArrayList<>(), 10000.0);
        marketRequest.setStock(stock);
        marketRequest.setAccount(account);

        Mockito.when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.of(stock));
        Mockito.when(accountRepository.findById(UUID.fromString("test_uuid"))).thenReturn(Optional.of(account));

        fireAndForgetController.processMarketRequest(marketRequest);

        assertEquals(90, marketRequest.getStock().getAmount());
        assertEquals(2000.0, marketRequest.getAccount().getFunds());
    }

    @Test
    public void processMarketRequestShouldHandleExceptions() {
        MarketRequest marketRequest = new MarketRequest();
        marketRequest.setStock(new Stock(UUID.randomUUID(), "Tesla", "TSLA", 200.0, 5L));
        marketRequest.setAccount(new Account(UUID.randomUUID(), new ArrayList<>(), 5000.0));

        Mockito.when(stockRepository.findByTicker("TSLA")).thenThrow(new EntityNotFoundException("Stock not found"));

        assertThrows(EntityNotFoundException.class, () -> fireAndForgetController.processMarketRequest(marketRequest));
    }
}
