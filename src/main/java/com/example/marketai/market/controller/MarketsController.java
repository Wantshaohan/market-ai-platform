package com.example.marketai.market.controller;

import com.example.marketai.common.response.Result;
import com.example.marketai.market.dto.CommodityDTO;
import com.example.marketai.market.dto.IndexOverviewDTO;
import com.example.marketai.market.service.MarketDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行情看板 - 指数概览与商品横条。
 */
@RestController
@RequestMapping("/api/v1/markets")
@RequiredArgsConstructor
public class MarketsController {

    private final MarketDashboardService marketDashboardService;

    /**
     * 全球指数概览。GET /api/v1/markets/overview?regions=US,EU,ASIA
     */
    @GetMapping("/overview")
    public Result<List<IndexOverviewDTO>> overview(@RequestParam(required = false) String regions) {
        return Result.success(marketDashboardService.getOverview(regions));
    }

    /**
     * 商品行情横条。GET /api/v1/markets/commodities?symbols=PLATINUM,SILVER,COPPER,PALLADIUM,RHODIUM
     */
    @GetMapping("/commodities")
    public Result<List<CommodityDTO>> commodities(@RequestParam(required = false) String symbols) {
        return Result.success(marketDashboardService.getCommodities(symbols));
    }
}
