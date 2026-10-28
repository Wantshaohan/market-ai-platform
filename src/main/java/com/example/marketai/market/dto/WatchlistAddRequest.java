package com.example.marketai.market.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增自选请求。
 */
@Data
public class WatchlistAddRequest {

    /** 标的代码，如 AAPL、000001.SS、GOLD */
    @NotBlank(message = "标的代码不能为空")
    private String symbol;
}
