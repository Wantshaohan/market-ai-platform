package com.example.marketai.market.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * K 线数据。
 */
@Data
public class KlineDTO {

    private String symbol;
    private String period;
    private long timestamp;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volume;
}
