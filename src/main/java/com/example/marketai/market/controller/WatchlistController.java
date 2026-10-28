package com.example.marketai.market.controller;

import com.example.marketai.common.response.Result;
import com.example.marketai.market.dto.WatchlistAddRequest;
import com.example.marketai.market.dto.WatchlistItemDTO;
import com.example.marketai.market.service.WatchlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行情看板 - 自选行情。
 */
@RestController
@RequestMapping("/api/v1/watchlists")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    /**
     * 自选列表。GET /api/v1/watchlists/default/items
     */
    @GetMapping("/default/items")
    public Result<List<WatchlistItemDTO>> list() {
        return Result.success(watchlistService.listItems());
    }

    /**
     * 新增自选。POST /api/v1/watchlists/default/items
     */
    @PostMapping("/default/items")
    public Result<WatchlistItemDTO> add(@Valid @RequestBody WatchlistAddRequest request) {
        return Result.success(watchlistService.addItem(request.getSymbol()));
    }

    /**
     * 删除自选。DELETE /api/v1/watchlists/default/items/{symbol}
     */
    @DeleteMapping("/default/items/{symbol}")
    public Result<Void> remove(@PathVariable String symbol) {
        watchlistService.removeItem(symbol);
        return Result.success();
    }
}
