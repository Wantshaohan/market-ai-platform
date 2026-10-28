package com.example.marketai.market.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * K 线 / 历史行情数据。
 */
@Data
public class CandleDTO {

    /** 该根 K 线的时间（epoch ms） */
    private long timestamp;

    /** 开盘价 */
    private BigDecimal open;

    /** 最高价 */
    private BigDecimal high;

    /** 最低价 */
    private BigDecimal low;

    /** 收盘价 */
    private BigDecimal close;

    /** 成交量 */
    private BigDecimal volume;
}
