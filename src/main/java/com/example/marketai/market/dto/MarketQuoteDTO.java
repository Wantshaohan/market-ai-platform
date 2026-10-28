package com.example.marketai.market.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 最新行情快照（主图头部与详情）。
 */
@Data
public class MarketQuoteDTO {

    /** 标的代码 */
    private String symbol;

    /** 标的名称 */
    private String name;

    /** 分类：指数 / 商品 / 股票 / 加密 */
    private String market;

    /** 计价货币 */
    private String currency;

    /** 计价单位 */
    private String unit;

    /** 最新价 */
    private BigDecimal price;

    /** 涨跌额 */
    private BigDecimal change;

    /** 涨跌幅（百分比） */
    private BigDecimal changePercent;

    /** 今开 */
    private BigDecimal open;

    /** 最高 */
    private BigDecimal high;

    /** 最低 */
    private BigDecimal low;

    /** 昨收 */
    private BigDecimal prevClose;

    /** 52 周最高 */
    private BigDecimal high52w;

    /** 52 周最低 */
    private BigDecimal low52w;

    /** 成交量 */
    private BigDecimal volume;

    /** 成交额 */
    private BigDecimal amount;

    /** 数据来源 */
    private String source;

    /** 行情更新时间（epoch ms） */
    private long updatedAt;
}
