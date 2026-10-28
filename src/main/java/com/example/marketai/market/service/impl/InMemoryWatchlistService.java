package com.example.marketai.market.service.impl;

import com.example.marketai.common.exception.BusinessException;
import com.example.marketai.common.exception.ErrorCode;
import com.example.marketai.market.dto.MarketQuoteDTO;
import com.example.marketai.market.dto.WatchlistItemDTO;
import com.example.marketai.market.service.MarketDashboardService;
import com.example.marketai.market.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 基于内存的自选行情实现。默认预置一组常见标的；重启后重置。
 * 生产环境可替换为数据库 + 用户维度持久化实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InMemoryWatchlistService implements WatchlistService {

    private final MarketDashboardService marketDashboardService;

    private final CopyOnWriteArrayList<String> symbols = new CopyOnWriteArrayList<>(
            List.of("AAPL", "TSLA", "NVDA", "00700.HK", "GOLD", "BTCUSD", "000001.SS"));

    @Override
    public List<WatchlistItemDTO> listItems() {
        return symbols.stream().map(this::toItem).toList();
    }

    @Override
    public WatchlistItemDTO addItem(String symbol) {
        String key = symbol.trim().toUpperCase();
        if (symbols.stream().anyMatch(s -> s.equalsIgnoreCase(key))) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "该标的已在自选中");
        }
        symbols.add(key);
        log.info("[WATCHLIST] 新增自选: {}", key);
        return toItem(key);
    }

    @Override
    public void removeItem(String symbol) {
        String key = symbol.trim().toUpperCase();
        boolean removed = symbols.removeIf(s -> s.equalsIgnoreCase(key));
        if (!removed) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "自选标的不存在");
        }
        log.info("[WATCHLIST] 删除自选: {}", key);
    }

    private WatchlistItemDTO toItem(String symbol) {
        MarketQuoteDTO q = marketDashboardService.getQuote(symbol);
        WatchlistItemDTO dto = new WatchlistItemDTO();
        dto.setSymbol(q.getSymbol());
        dto.setName(q.getName());
        dto.setMarket(q.getMarket());
        dto.setPrice(q.getPrice());
        dto.setChange(q.getChange());
        dto.setChangePercent(q.getChangePercent());
        dto.setUpdatedAt(q.getUpdatedAt());
        return dto;
    }
}
