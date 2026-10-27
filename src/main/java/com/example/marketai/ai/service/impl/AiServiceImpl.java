package com.example.marketai.ai.service.impl;

import com.example.marketai.ai.dto.AiAnalysisRequest;
import com.example.marketai.ai.dto.AiAnalysisResponse;
import com.example.marketai.ai.prompt.PromptTemplates;
import com.example.marketai.ai.provider.AiProvider;
import com.example.marketai.ai.service.AiService;
import com.example.marketai.market.dto.QuoteDTO;
import com.example.marketai.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * AI 分析服务实现：聚合行情数据 -> 组装提示词 -> 调用模型 -> 返回结果。
 */
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final AiProvider aiProvider;
    private final MarketService marketService;

    @Override
    public AiAnalysisResponse analyze(AiAnalysisRequest request) {
        QuoteDTO quote = marketService.getQuote(request.getSymbol());
        String userPrompt = PromptTemplates.buildAnalysisPrompt(
                request.getSymbol(), describeQuote(quote), request.getQuestion());
        String analysis = aiProvider.chat(PromptTemplates.MARKET_ANALYSIS_SYSTEM, userPrompt);

        return AiAnalysisResponse.builder()
                .symbol(request.getSymbol())
                .analysis(analysis)
                .build();
    }

    private String describeQuote(QuoteDTO quote) {
        return String.format("现价 %s，涨跌幅 %s%%，最高 %s，最低 %s",
                quote.getPrice(), quote.getChangePercent(), quote.getHigh(), quote.getLow());
    }
}
