package com.example.marketai.ai.service;

import com.example.marketai.ai.dto.AiAnalysisRequest;
import com.example.marketai.ai.dto.AiAnalysisResponse;

/**
 * AI 分析服务。
 */
public interface AiService {

    AiAnalysisResponse analyze(AiAnalysisRequest request);
}
