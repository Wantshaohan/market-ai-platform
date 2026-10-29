package com.example.marketai.ai.service.impl;

import com.example.marketai.ai.dto.AiAnalysisRequest;
import com.example.marketai.ai.dto.AiAnalysisResponse;
import com.example.marketai.ai.prompt.PromptTemplates;
import com.example.marketai.ai.provider.AiProvider;
import com.example.marketai.ai.service.AiService;
import com.example.marketai.market.dto.CandleDTO;
import com.example.marketai.market.dto.ForexQuoteDTO;
import com.example.marketai.market.dto.MarketQuoteDTO;
import com.example.marketai.market.service.ForexService;
import com.example.marketai.market.service.MarketDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AI 分析服务实现：聚合当前盘面（外汇缓存 / 行情看板）→ 组装提示词 → 调用模型 → 返回结果。
 */
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final AiProvider aiProvider;
    private final MarketDashboardService marketDashboardService;
    private final ForexService forexService;

    @Override
    public AiAnalysisResponse analyze(AiAnalysisRequest request) {
        String symbol = request.getSymbol();
        String marketSummary = buildMarketSummary(symbol);
        String userPrompt = PromptTemplates.buildAnalysisPromptFromSummary(symbol, marketSummary, request.getQuestion());
        String analysis = aiProvider.chat(PromptTemplates.MARKET_ANALYSIS_SYSTEM, userPrompt);

        return AiAnalysisResponse.builder()
                .symbol(symbol)
                .analysis(analysis)
                .build();
    }

    private String buildMarketSummary(String symbol) {
        ForexQuoteDTO forex = forexService.getQuote(symbol);
        if (forex != null) {
            return describeForex(forex);
        }
        return describeDashboard(symbol);
    }

    private String describeForex(ForexQuoteDTO q) {
        List<java.math.BigDecimal> spark = q.getSparkline();
        String trend = (spark == null || spark.size() < 2)
                ? "暂无趋势数据"
                : "最近收盘价序列：" + spark.subList(Math.max(0, spark.size() - 10), spark.size());
        return String.format("外汇 %s（%s）：现价 %s，涨跌幅 %s%%，最高 %s，最低 %s。%s",
                q.getSymbol(), q.getName(), q.getPrice(), q.getChangePercent(), q.getHigh(), q.getLow(), trend);
    }

    private String describeDashboard(String symbol) {
        MarketQuoteDTO quote = marketDashboardService.getQuote(symbol);
        List<CandleDTO> candles = marketDashboardService.getCandles(symbol, "1d", null, null, 60);
        int from = Math.max(0, candles.size() - 10);
        String closes = candles.subList(from, candles.size()).stream()
                .map(c -> c.getClose().toPlainString())
                .collect(Collectors.joining(", "));
        return String.format("标的 %s（%s）：现价 %s，涨跌幅 %s%%，最高 %s，最低 %s，昨收 %s。近 %d 个交易日收盘价：%s",
                quote.getSymbol(), quote.getName(), quote.getPrice(), quote.getChangePercent(),
                quote.getHigh(), quote.getLow(), quote.getPrevClose(), candles.size() - from, closes);
    }
}
