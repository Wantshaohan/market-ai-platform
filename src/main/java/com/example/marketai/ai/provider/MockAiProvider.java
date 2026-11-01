package com.example.marketai.ai.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

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

    @Override
    public void chatStream(String systemPrompt, String userPrompt, Consumer<String> onToken) {
        log.info("[MOCK AI] stream system={}, user={}", systemPrompt, userPrompt);
        String text = buildText(userPrompt);
        for (int i = 0; i < text.length(); i++) {
            onToken.accept(String.valueOf(text.charAt(i)));
            sleep(15);
        }
    }

    private String buildText(String userPrompt) {
        boolean advice = userPrompt != null && userPrompt.contains("投资建议");
        if (advice) {
            return "（模拟 AI）基于当前盘面，给出如下参考思路（不构成投资建议）：\n"
                    + "1. 仓位控制：建议总仓位控制在五成以内，避免单一标的过度集中；\n"
                    + "2. 止损纪律：可参考近 20 日低点下方设置止损，跌破及时离场；\n"
                    + "3. 分批布局：若看好中期趋势，可分两到三批逢回调低吸；\n"
                    + "4. 关注信号：等待量能放大与均线多头排列确认后再加仓。";
        }
        return "（模拟 AI）当前行情整体处于震荡整理阶段，短期方向暂不明确：\n"
                + "1. 趋势：均线粘合，价格在区间内反复，未形成有效突破；\n"
                + "2. 量能：成交温和，缺乏增量资金，需警惕假突破；\n"
                + "3. 支撑/压力：关注前期高低点的支撑与压力位；\n"
                + "4. 情绪：市场情绪偏谨慎，建议等待右侧信号确认。";
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
