package com.example.marketai.market.service;

import com.example.marketai.market.dto.WatchlistItemDTO;

import java.util.List;

/**
 * 自选行情服务。当前为内存实现（模拟），生产环境可替换为基于数据库的持久化实现。
 */
public interface WatchlistService {

    List<WatchlistItemDTO> listItems();

    WatchlistItemDTO addItem(String symbol);

    void removeItem(String symbol);
}
