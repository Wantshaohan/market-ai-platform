package com.example.marketai.ai.provider;

import com.example.marketai.ai.config.DeepSeekProperties;
import com.example.marketai.common.exception.BusinessException;
import com.example.marketai.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * DeepSeek 提供商，通过其 Anthropic 兼容端点（POST {base-url}/v1/messages）调用。
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "market-ai.ai.provider", havingValue = "deepseek")
public class DeepSeekProvider implements AiProvider {

    private final DeepSeekProperties properties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @SuppressWarnings("unchecked")
    public String chat(String systemPrompt, String userPrompt) {
        if (!StringUtils.hasText(properties.getApiKey())) {
            throw new BusinessException(ErrorCode.AI_PROVIDER_ERROR, "未配置 DeepSeek apiKey");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", properties.getApiKey());
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> body = Map.of(
                "model", properties.getModel(),
                "max_tokens", properties.getMaxTokens(),
                "system", systemPrompt,
                "messages", List.of(Map.of("role", "user", "content", userPrompt))
        );

        String url = baseUrl() + "/v1/messages";
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), Map.class);
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");
            if (content == null || content.isEmpty()) {
                throw new IllegalStateException("DeepSeek 返回 content 为空");
            }
            Object text = content.get(0).get("text");
            if (text == null) {
                throw new IllegalStateException("DeepSeek 返回文本为空");
            }
            return text.toString();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 DeepSeek 失败", e);
            throw new BusinessException(ErrorCode.AI_PROVIDER_ERROR, "DeepSeek 服务调用失败: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void chatStream(String systemPrompt, String userPrompt, Consumer<String> onToken) {
        if (!StringUtils.hasText(properties.getApiKey())) {
            throw new BusinessException(ErrorCode.AI_PROVIDER_ERROR, "未配置 DeepSeek apiKey");
        }

        Map<String, Object> body = Map.of(
                "model", properties.getModel(),
                "max_tokens", properties.getMaxTokens(),
                "system", systemPrompt,
                "messages", List.of(Map.of("role", "user", "content", userPrompt)),
                "stream", true
        );

        String url = baseUrl() + "/v1/messages";
        try {
            restTemplate.execute(url, HttpMethod.POST, request -> {
                request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                request.getHeaders().set("x-api-key", properties.getApiKey());
                request.getHeaders().set("anthropic-version", "2023-06-01");
                request.getHeaders().setAccept(List.of(MediaType.TEXT_EVENT_STREAM));
                objectMapper.writeValue(request.getBody(), body);
            }, response -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith("data:")) {
                            continue;
                        }
                        String json = line.substring(5).trim();
                        if (json.isEmpty()) {
                            continue;
                        }
                        Map<String, Object> event = objectMapper.readValue(json, Map.class);
                        if (!"content_block_delta".equals(event.get("type"))) {
                            continue;
                        }
                        Object delta = event.get("delta");
                        if (!(delta instanceof Map)) {
                            continue;
                        }
                        Object text = ((Map<String, Object>) delta).get("text");
                        if (text != null) {
                            onToken.accept(text.toString());
                        }
                    }
                }
                return null;
            });
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 DeepSeek 流式接口失败", e);
            throw new BusinessException(ErrorCode.AI_PROVIDER_ERROR, "DeepSeek 服务调用失败: " + e.getMessage());
        }
    }

    private String baseUrl() {
        String base = properties.getBaseUrl();
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }
}
