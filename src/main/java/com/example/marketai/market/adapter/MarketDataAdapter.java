package com.example.marketai.market.adapter;

import com.example.marketai.market.dto.KlineDTO;
import com.example.marketai.market.dto.QuoteDTO;

import java.util.List;

/**
 * 第三方行情数据适配器。不同行情源（东财/新浪/聚合数据等）通过实现该接口接入。
 */
public interface MarketDataAdapter {

    QuoteDTO getQuote(String symbol);

    List<KlineDTO> getKline(String symbol, String period, int limit);
}
