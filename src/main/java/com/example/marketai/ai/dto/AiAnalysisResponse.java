package com.example.marketai.ai.dto;

import lombok.Builder;
import lombok.Data;

/**
 * AI 分析响应。
 */
@Data
@Builder
public class AiAnalysisResponse {

    private String symbol;
    private String analysis;
}
