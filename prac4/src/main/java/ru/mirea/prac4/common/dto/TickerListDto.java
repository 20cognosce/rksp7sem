package ru.mirea.prac4.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class TickerListDto {

    List<String> tickers;
}
