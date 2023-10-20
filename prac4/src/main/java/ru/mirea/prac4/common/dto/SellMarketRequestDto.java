package ru.mirea.prac4.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellMarketRequestDto {

    Integer stocksSold;
    String ticker;
    Double totalPrice;
}
