package com.example.marketai.market.controller;

import com.example.marketai.common.response.Result;
import com.example.marketai.market.dto.CandleDTO;
import com.example.marketai.market.dto.MarketQuoteDTO;
import com.example.marketai.market.service.MarketDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

/**
 * 行情看板 - 最新行情与历史 K 线。
 */
@RestController
@RequiredArgsConstructor
public class QuotesController {

    private final MarketDashboardService marketDashboardService;

    /**
     * 主图最新行情。GET /api/v1/quotes/{symbol}
     */
    @GetMapping("/api/v1/quotes/{symbol}")
    public Result<MarketQuoteDTO> quote(@PathVariable String symbol) {
        return Result.success(marketDashboardService.getQuote(symbol));
    }

    /**
     * 主图历史数据。
     * GET /api/v1/market-data/{symbol}/candles?interval=1d&from=2026-10-01T00:00:00Z&to=2026-10-28T23:59:59Z&limit=500
     */
    @GetMapping("/api/v1/market-data/{symbol}/candles")
    public Result<List<CandleDTO>> candles(@PathVariable String symbol,
                                           @RequestParam(defaultValue = "1d") String interval,
                                           @RequestParam(required = false) String from,
                                           @RequestParam(required = false) String to,
                                           @RequestParam(defaultValue = "500") int limit) {
        return Result.success(marketDashboardService.getCandles(symbol, interval, parseTime(from), parseTime(to), limit));
    }

    /** 支持 ISO-8601（如 2026-10-01T00:00:00Z）或 epoch 毫秒；无法解析时返回 null（由服务端使用默认值）。 */
    private Long parseTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(value).toEpochMilli();
        } catch (Exception ignored) {
            // fallthrough
        }
        try {
            return Long.parseLong(value);
        } catch (Exception ignored) {
            // fallthrough
        }
        return null;
    }
}
