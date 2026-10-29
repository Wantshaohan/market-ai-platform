package com.example.marketai.market.controller;

import com.example.marketai.common.response.Result;
import com.example.marketai.market.dto.ForexQuoteDTO;
import com.example.marketai.market.service.ForexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 外汇行情接口（从缓存读取）。
 */
@RestController
@RequestMapping("/api/v1/forex")
@RequiredArgsConstructor
public class ForexController {

    private final ForexService forexService;

    /**
     * 固定展示的外汇行情快照。GET /api/v1/forex/quotes
     */
    @GetMapping("/quotes")
    public Result<List<ForexQuoteDTO>> quotes() {
        return Result.success(forexService.getQuotes());
    }
}
