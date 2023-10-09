package ru.mirea.prac4.server.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.prac4.common.Stock;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends CrudRepository<Stock, UUID> {

    Optional<Stock> findByTicker(String ticker);
}
