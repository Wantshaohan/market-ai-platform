package com.example.marketai.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * DeepSeek（Anthropic 兼容端点）配置。Apollo 就绪：后续接入配置中心时
 * 迁移 market-ai.ai.deepseek.* 同名 key 即可。
 */
@Data
@Component
@ConfigurationProperties(prefix = "market-ai.ai.deepseek")
public class DeepSeekProperties {

    /** API Key，由用户自行填写 */
    private String apiKey = "";

    /** Anthropic 兼容端点基础地址 */
    private String baseUrl = "https://api.deepseek.com/anthropic";

    /** 模型名（deepseek-chat / deepseek-flash / deepseek-v4-pro） */
    private String model = "deepseek-chat";

    /** 最大生成 token 数 */
    private int maxTokens = 1024;
}
