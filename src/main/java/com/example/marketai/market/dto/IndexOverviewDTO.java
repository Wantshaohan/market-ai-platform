package com.example.marketai.market.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 全球指数概览条目（顶部指数卡片）。
 */
@Data
public class IndexOverviewDTO {

    /** 指数代码，如 SPX、000001.SS */
    private String symbol;

    /** 指数名称，如 标普500、上证指数 */
    private String name;

    /** 区域：US / EU / ASIA */
    private String region;

    /** 计价货币，如 USD / CNY / HKD / JPY / EUR / GBP */
    private String currency;

    /** 最新价 */
    private BigDecimal price;

    /** 涨跌额 */
    private BigDecimal change;

    /** 涨跌幅（百分比） */
    private BigDecimal changePercent;

    /** 行情更新时间（epoch ms） */
    private long updatedAt;
}
