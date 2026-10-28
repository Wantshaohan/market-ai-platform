package com.example.marketai.market.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 自选行情条目。
 */
@Data
public class WatchlistItemDTO {

    /** 标的代码 */
    private String symbol;

    /** 标的名称 */
    private String name;

    /** 分类：指数 / 商品 / 股票 / 加密 */
    private String market;

    /** 最新价 */
    private BigDecimal price;

    /** 涨跌额 */
    private BigDecimal change;

    /** 涨跌幅（百分比） */
    private BigDecimal changePercent;

    /** 行情更新时间（epoch ms） */
    private long updatedAt;
}
