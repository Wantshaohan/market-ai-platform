package com.example.marketai.market.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品行情条目（商品横条）。
 */
@Data
public class CommodityDTO {

    /** 商品代码，如 GOLD、SILVER、COPPER */
    private String symbol;

    /** 商品名称，如 黄金、白银、铜 */
    private String name;

    /** 分类：PRECIOUS（贵金属）/ INDUSTRIAL（工业金属） */
    private String category;

    /** 计价单位，如 美元/盎司、美元/磅 */
    private String unit;

    /** 最新价 */
    private BigDecimal price;

    /** 涨跌额 */
    private BigDecimal change;

    /** 涨跌幅（百分比） */
    private BigDecimal changePercent;

    /** 行情更新时间（epoch ms） */
    private long updatedAt;
}
