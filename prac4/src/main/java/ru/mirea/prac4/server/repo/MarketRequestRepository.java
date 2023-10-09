package ru.mirea.prac4.server.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.prac4.common.MarketRequest;

import java.util.UUID;

@Repository
public interface MarketRequestRepository extends CrudRepository<MarketRequest, UUID> {
}
