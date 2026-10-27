package com.example.marketai.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * AI 分析请求。
 */
@Data
public class AiAnalysisRequest {

    @NotBlank(message = "标的代码不能为空")
    private String symbol;

    /** 可选的自定义提问，为空则使用默认分析 */
    private String question;
}
