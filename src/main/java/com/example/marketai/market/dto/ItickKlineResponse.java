package com.example.marketai.market.dto;

import lombok.Data;

import java.util.List;

/**
 * itick 行情源 K 线接口响应壳（外部约定：code=0 成功）。
 */
@Data
public class ItickKlineResponse {

    private int code;

    private String msg;

    private List<ForexKlineDTO> data;
}
