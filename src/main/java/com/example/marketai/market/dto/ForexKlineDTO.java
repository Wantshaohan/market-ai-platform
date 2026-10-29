package com.example.marketai.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * itick 行情源单根 K 线（原始字段映射）。
 * 原始字段：tu 成交金额 / c 收盘 / t 时间戳 / v 成交量 / h 最高 / l 最低 / o 开盘。
 */
@Data
public class ForexKlineDTO {

    @JsonProperty("tu")
    private BigDecimal amount;

    @JsonProperty("c")
    private BigDecimal close;

    @JsonProperty("t")
    private long timestamp;

    @JsonProperty("v")
    private BigDecimal volume;

    @JsonProperty("h")
    private BigDecimal high;

    @JsonProperty("l")
    private BigDecimal low;

    @JsonProperty("o")
    private BigDecimal open;
}
