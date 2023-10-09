package ru.mirea.prac4.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Table(name = "market_request")
@Entity
public class MarketRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @JoinColumn(name = "account_uuid", referencedColumnName = "uuid")
    @OneToOne
    private Account account;

    @JoinColumn(name = "stock_uuid", referencedColumnName = "uuid")
    @OneToOne
    private Stock stock;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
