package com.example.marketai.market.service;

import com.example.marketai.market.dto.CandleDTO;
import com.example.marketai.market.dto.CommodityDTO;
import com.example.marketai.market.dto.IndexOverviewDTO;
import com.example.marketai.market.dto.MarketQuoteDTO;

import java.util.List;

/**
 * 行情看板服务。为前端行情页提供概览、商品、实时行情与历史 K 线数据。
 */
public interface MarketDashboardService {

    /**
     * 全球指数概览。
     *
     * @param regions 逗号分隔的区域，如 "US,EU,ASIA"；为空则返回全部
     */
    List<IndexOverviewDTO> getOverview(String regions);

    /**
     * 商品行情。
     *
     * @param symbols 逗号分隔的商品代码，如 "GOLD,SILVER,COPPER"；为空则返回全部
     */
    List<CommodityDTO> getCommodities(String symbols);

    /**
     * 最新行情快照。
     */
    MarketQuoteDTO getQuote(String symbol);

    /**
     * 历史 K 线数据。
     *
     * @param interval 周期：1m/5m/15m/30m/1h/1d/1w/1M
     * @param fromMs   起始时间（epoch ms，可为 null）
     * @param toMs     结束时间（epoch ms，可为 null）
     * @param limit    最大返回条数
     */
    List<CandleDTO> getCandles(String symbol, String interval, Long fromMs, Long toMs, int limit);
}
