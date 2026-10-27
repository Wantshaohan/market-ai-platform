package com.example.marketai.market.service;

import com.example.marketai.market.dto.KlineDTO;
import com.example.marketai.market.dto.QuoteDTO;

import java.util.List;

/**
 * 行情聚合服务。
 */
public interface MarketService {

    QuoteDTO getQuote(String symbol);

    List<KlineDTO> getKline(String symbol, String period, int limit);
}
