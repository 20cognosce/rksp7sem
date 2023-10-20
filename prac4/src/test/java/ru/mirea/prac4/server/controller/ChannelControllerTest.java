package ru.mirea.prac4.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.mirea.prac4.common.Account;
import ru.mirea.prac4.common.Account2Stock;
import ru.mirea.prac4.common.MarketRequest;
import ru.mirea.prac4.common.Stock;
import ru.mirea.prac4.common.dto.SellMarketRequestDto;
import ru.mirea.prac4.common.util.JsonUtil;
import ru.mirea.prac4.server.repo.AccountRepository;
import ru.mirea.prac4.server.repo.MarketRequestRepository;
import ru.mirea.prac4.server.repo.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ChannelControllerTest {
    private ChannelController channelController;

    @Mock
    private MarketRequestRepository marketRequestRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        channelController = new ChannelController(marketRequestRepository, stockRepository, accountRepository);
    }

    @Test
    void testProcessMarketRequest() {
        MarketRequest marketRequest = new MarketRequest();
        var account = new Account(UUID.randomUUID(), "John Doe", new ArrayList<>(), 10000.0);
        var stock = new Stock(UUID.randomUUID(), "Apple", "AAPL", 100.0, 10L);
        account.setAccount2Stocks(List.of(new Account2Stock(UUID.randomUUID(), account, stock, 20)));
        marketRequest.setAmount(10);
        marketRequest.setAccount(account);
        marketRequest.setStock(stock);

        when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.of(stock));
        when(accountRepository.findByName("John Doe")).thenReturn(Optional.of(account));

        String response = channelController.processMarketRequest(marketRequest);

        assertEquals(JsonUtil.writeJson(new SellMarketRequestDto(10, "AAPL", 1000.0)), response);

        Mockito.verify(stockRepository, Mockito.times(1)).save(stock);
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
        Mockito.verify(marketRequestRepository, Mockito.times(1)).save(marketRequest);
    }

    @Test
    void testCreateSellMarketRequest() {
        MarketRequest marketRequest = new MarketRequest();
        var account = new Account(UUID.randomUUID(), "John Doe", new ArrayList<>(), 10000.0);
        var stock = new Stock(UUID.randomUUID(), "Apple", "AAPL", 100.0, 10L);
        account.setAccount2Stocks(List.of(new Account2Stock(UUID.randomUUID(), account, stock, 20)));
        marketRequest.setAmount(10);
        marketRequest.setAccount(account);
        marketRequest.setStock(stock);

        Flux<String> marketRequestFlux = Flux.just(JsonUtil.writeJson(marketRequest));

        when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.of(stock));
        when(accountRepository.findByName("John Doe")).thenReturn(Optional.of(account));
        when(marketRequestRepository.save(marketRequest)).thenReturn(marketRequest);

        Flux<String> responseFlux = channelController.createSellMarketRequest(marketRequestFlux);

        StepVerifier.create(responseFlux)
                .expectNextMatches(response -> response.equals(
                        JsonUtil.writeJson(new SellMarketRequestDto(10, "AAPL", 1000.0))))
                .verifyComplete();
    }
}
