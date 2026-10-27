package com.example.marketai.market.controller;

import com.example.marketai.common.response.Result;
import com.example.marketai.market.dto.KlineDTO;
import com.example.marketai.market.dto.QuoteDTO;
import com.example.marketai.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行情接口。
 */
@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/quote")
    public Result<QuoteDTO> quote(@RequestParam String symbol) {
        return Result.success(marketService.getQuote(symbol));
    }

    @GetMapping("/kline")
    public Result<List<KlineDTO>> kline(@RequestParam String symbol,
                                        @RequestParam(defaultValue = "day") String period,
                                        @RequestParam(defaultValue = "60") int limit) {
        return Result.success(marketService.getKline(symbol, period, limit));
    }
}
