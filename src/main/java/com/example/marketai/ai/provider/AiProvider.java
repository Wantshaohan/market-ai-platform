package com.example.marketai.ai.provider;

/**
 * 大模型适配器。不同模型服务（OpenAI / 通义 / 文心 / 本地模型等）通过实现该接口接入。
 */
public interface AiProvider {

    String chat(String systemPrompt, String userPrompt);
}
