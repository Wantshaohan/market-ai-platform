package com.example.marketai.market.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 外汇行情快照（前端外汇面板展示项）。
 */
@Data
public class ForexQuoteDTO {

    /** 产品代码，如 EURUSD */
    private String symbol;

    /** 展示名称 */
    private String name;

    /** 分类：forex / index / stock */
    private String category;

    /** 最新价（最近一根 K 线收盘价） */
    private BigDecimal price;

    /** 涨跌额（相对上一根 K 线） */
    private BigDecimal change;

    /** 涨跌幅（百分比） */
    private BigDecimal changePercent;

    /** 最近一根 K 线开盘价 */
    private BigDecimal open;

    /** 最高价 */
    private BigDecimal high;

    /** 最低价 */
    private BigDecimal low;

    /** 成交量 */
    private BigDecimal volume;

    /** 成交金额 */
    private BigDecimal amount;

    /** 行情更新时间（epoch ms） */
    private long updatedAt;

    /** 最近收盘价序列（迷你走势图） */
    private List<BigDecimal> sparkline;
}
