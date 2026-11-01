package com.example.marketai.ai.service;

import com.example.marketai.ai.dto.AiAnalysisRequest;
import com.example.marketai.ai.dto.AiAnalysisResponse;

import java.util.function.Consumer;

/**
 * AI 分析服务。
 */
public interface AiService {

    AiAnalysisResponse analyze(AiAnalysisRequest request);

    /**
     * 流式分析：组装提示词后调用模型，将增量文本逐个回调给 {@code onToken}。
     */
    void analyzeStream(AiAnalysisRequest request, Consumer<String> onToken);
}
