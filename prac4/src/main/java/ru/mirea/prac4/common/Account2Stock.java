package ru.mirea.prac4.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "account_stock")
@Entity
public class Account2Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @JsonIgnore
    @JoinColumn(name = "account_uuid", referencedColumnName = "uuid")
    @ManyToOne
    private Account account;

    @JoinColumn(name = "stock_uuid", referencedColumnName = "uuid")
    @ManyToOne
    private Stock stock;

    @Column(name = "amount")
    private Integer amount;
}
