package com.example.marketai.market.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 实时行情快照。
 */
@Data
public class QuoteDTO {

    private String symbol;
    private String name;
    private BigDecimal price;
    private BigDecimal change;
    private BigDecimal changePercent;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal prevClose;
    private long timestamp;
}
