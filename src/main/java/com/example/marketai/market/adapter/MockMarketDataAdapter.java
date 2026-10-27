package com.example.marketai.market.adapter;

import com.example.marketai.market.dto.KlineDTO;
import com.example.marketai.market.dto.QuoteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 模拟行情适配器（开发环境）。
 * 生产环境请替换为真实行情源实现，如 {@code EastMoneyMarketDataAdapter}。
 */
@Slf4j
@Component
public class MockMarketDataAdapter implements MarketDataAdapter {

    @Override
    public QuoteDTO getQuote(String symbol) {
        log.info("[MOCK MARKET] 查询实时行情: {}", symbol);
        QuoteDTO quote = new QuoteDTO();
        quote.setSymbol(symbol);
        quote.setName("示例标的");
        quote.setPrevClose(new BigDecimal("10.00"));
        quote.setOpen(new BigDecimal("10.02"));
        quote.setPrice(new BigDecimal("10.25"));
        quote.setHigh(new BigDecimal("10.40"));
        quote.setLow(new BigDecimal("9.95"));
        quote.setChange(new BigDecimal("0.25"));
        quote.setChangePercent(new BigDecimal("2.50"));
        quote.setTimestamp(System.currentTimeMillis());
        return quote;
    }

    @Override
    public List<KlineDTO> getKline(String symbol, String period, int limit) {
        log.info("[MOCK MARKET] 查询K线: symbol={}, period={}, limit={}", symbol, period, limit);
        List<KlineDTO> list = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            KlineDTO kline = new KlineDTO();
            kline.setSymbol(symbol);
            kline.setPeriod(period);
            kline.setOpen(new BigDecimal("10.00"));
            kline.setHigh(new BigDecimal("10.30"));
            kline.setLow(new BigDecimal("9.90"));
            kline.setClose(new BigDecimal("10.20"));
            kline.setVolume(new BigDecimal("1000000"));
            kline.setTimestamp(System.currentTimeMillis() - (limit - i) * 86400000L);
            list.add(kline);
        }
        return list;
    }
}
