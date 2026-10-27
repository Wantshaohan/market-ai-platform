package com.example.marketai.ai.prompt;

/**
 * 提示词模板。集中管理，便于统一调优。
 */
public final class PromptTemplates {

    private PromptTemplates() {
    }

    public static final String MARKET_ANALYSIS_SYSTEM =
            "你是一名专业的金融市场分析师，擅长技术面与基本面分析。"
                    + "请基于提供的行情数据给出客观、审慎的分析，不构成任何投资建议。";

    public static String buildAnalysisPrompt(String symbol, String quoteDesc, String question) {
        String q = (question == null || question.isBlank()) ? "请对该标的进行综合分析。" : question;
        return String.format("标的：%s%n最新行情：%s%n用户问题：%s", symbol, quoteDesc, q);
    }
}
