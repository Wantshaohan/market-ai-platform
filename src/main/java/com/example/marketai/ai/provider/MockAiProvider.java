package com.example.marketai.ai.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 模拟 AI 提供商（开发环境）。
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "market-ai.ai.provider", havingValue = "mock", matchIfMissing = true)
public class MockAiProvider implements AiProvider {

    @Override
    public String chat(String systemPrompt, String userPrompt) {
        log.info("[MOCK AI] system={}, user={}", systemPrompt, userPrompt);
        return "（模拟 AI 分析结果）当前标的整体趋势震荡，短期需关注量能配合，"
                + "建议结合基本面与市场情绪综合判断，注意控制仓位、设置止损。";
    }
}
