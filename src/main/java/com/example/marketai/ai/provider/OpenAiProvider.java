package com.example.marketai.ai.provider;

import com.example.marketai.common.exception.BusinessException;
import com.example.marketai.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
 * OpenAI 兼容接口实现（占位，配置 apiKey 后可启用）。
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "market-ai.ai.provider", havingValue = "openai")
public class OpenAiProvider implements AiProvider {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${market-ai.ai.openai.api-key:}")
    private String apiKey;

    @Value("${market-ai.ai.openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${market-ai.ai.openai.model:gpt-4o}")
    private String model;

    @Override
    @SuppressWarnings("unchecked")
    public String chat(String systemPrompt, String userPrompt) {
        if (!StringUtils.hasText(apiKey)) {
            throw new BusinessException(ErrorCode.AI_PROVIDER_ERROR, "未配置 OpenAI apiKey");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)),
                "temperature", 0.7
        );

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    baseUrl + "/chat/completions", new HttpEntity<>(body, headers), Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> first = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) first.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            log.error("调用 AI 服务失败", e);
            throw new BusinessException(ErrorCode.AI_PROVIDER_ERROR, "AI 服务调用失败: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void chatStream(String systemPrompt, String userPrompt, Consumer<String> onToken) {
        if (!StringUtils.hasText(apiKey)) {
            throw new BusinessException(ErrorCode.AI_PROVIDER_ERROR, "未配置 OpenAI apiKey");
        }

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)),
                "temperature", 0.7,
                "stream", true
        );

        try {
            restTemplate.execute(baseUrl + "/chat/completions", HttpMethod.POST, request -> {
                request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                request.getHeaders().setBearerAuth(apiKey);
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
                        if (json.isEmpty() || "[DONE]".equals(json)) {
                            continue;
                        }
                        Map<String, Object> event = objectMapper.readValue(json, Map.class);
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) event.get("choices");
                        if (choices == null || choices.isEmpty()) {
                            continue;
                        }
                        Object delta = choices.get(0).get("delta");
                        if (!(delta instanceof Map)) {
                            continue;
                        }
                        Object content = ((Map<String, Object>) delta).get("content");
                        if (content != null) {
                            onToken.accept(content.toString());
                        }
                    }
                }
                return null;
            });
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 AI 流式接口失败", e);
            throw new BusinessException(ErrorCode.AI_PROVIDER_ERROR, "AI 服务调用失败: " + e.getMessage());
        }
    }
}
