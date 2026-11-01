package com.example.marketai.ai.provider;

import java.util.function.Consumer;

/**
 * 大模型适配器。不同模型服务（OpenAI / 通义 / 文心 / 本地模型等）通过实现该接口接入。
 */
public interface AiProvider {

    String chat(String systemPrompt, String userPrompt);

    /**
     * 流式输出。默认实现退回为一次性调用 {@link #chat} 后按块切分输出，
     * 保证未实现流式接口的提供商也能获得渐进式展示效果。
     */
    default void chatStream(String systemPrompt, String userPrompt, Consumer<String> onToken) {
        String full = chat(systemPrompt, userPrompt);
        if (full == null || full.isEmpty()) {
            return;
        }
        int step = 8;
        for (int i = 0; i < full.length(); i += step) {
            onToken.accept(full.substring(i, Math.min(full.length(), i + step)));
        }
    }
}
