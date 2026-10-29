package com.example.marketai.market.service;

import com.example.marketai.market.dto.ForexQuoteDTO;

import java.util.List;

/**
 * 外汇行情服务：从第三方行情源拉取 K 线写入缓存，并从缓存读取供页面/AI 使用。
 */
public interface ForexService {

    /** 拉取全部配置标的的 K 线并写入缓存 */
    void refresh();

    /** 从缓存读取全部配置标的最新行情快照 */
    List<ForexQuoteDTO> getQuotes();

    /** 从缓存读取单个标的行情快照（供 AI 分析），无数据时返回 null */
    ForexQuoteDTO getQuote(String symbol);
}
