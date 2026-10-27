package com.example.marketai.market.service.impl;

import com.example.marketai.market.adapter.MarketDataAdapter;
import com.example.marketai.market.dto.KlineDTO;
import com.example.marketai.market.dto.QuoteDTO;
import com.example.marketai.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行情聚合服务实现，通过适配器隔离第三方行情源。
 */
@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketDataAdapter marketDataAdapter;

    @Override
    public QuoteDTO getQuote(String symbol) {
        return marketDataAdapter.getQuote(symbol);
    }

    @Override
    public List<KlineDTO> getKline(String symbol, String period, int limit) {
        return marketDataAdapter.getKline(symbol, period, limit);
    }
}
