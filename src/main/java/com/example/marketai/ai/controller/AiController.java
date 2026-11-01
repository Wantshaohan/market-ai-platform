package com.example.marketai.ai.controller;

import com.example.marketai.ai.dto.AiAnalysisRequest;
import com.example.marketai.ai.dto.AiAnalysisResponse;
import com.example.marketai.ai.service.AiService;
import com.example.marketai.common.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AI 分析接口。
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final ObjectMapper objectMapper;

    @PostMapping("/analyze")
    public Result<AiAnalysisResponse> analyze(@Valid @RequestBody AiAnalysisRequest request) {
        return Result.success(aiService.analyze(request));
    }

    /**
     * 流式分析（SSE）。事件：start（标的）→ delta（增量文本）→ done / error。
     * 每个事件的 data 均为 JSON 对象，便于前端统一解析。
     */
    @PostMapping(value = "/analyze/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter analyzeStream(@Valid @RequestBody AiAnalysisRequest request) {
        SseEmitter emitter = new SseEmitter(120_000L);
        AtomicBoolean closed = new AtomicBoolean(false);

        emitter.onCompletion(() -> closed.set(true));
        emitter.onError(e -> closed.set(true));

        CompletableFuture.runAsync(() -> {
            try {
                emit(emitter, closed, "start", Map.of("symbol", request.getSymbol()));
                aiService.analyzeStream(request, token -> emit(emitter, closed, "delta", Map.of("t", token)));
                emit(emitter, closed, "done", Map.of());
            } catch (Exception e) {
                log.error("AI 流式分析失败", e);
                emit(emitter, closed, "error", Map.of("message", e.getMessage() == null ? "AI 分析失败" : e.getMessage()));
            } finally {
                if (!closed.get()) {
                    emitter.complete();
                }
            }
        });

        return emitter;
    }

    private void emit(SseEmitter emitter, AtomicBoolean closed, String name, Object data) {
        if (closed.get()) {
            return;
        }
        try {
            // 显式序列化为 JSON 字符串再作为 data 发送，避免依赖 SseEmitter 对非 String 数据的序列化行为
            String json = objectMapper.writeValueAsString(data);
            emitter.send(SseEmitter.event().name(name).data(json));
        } catch (Exception e) {
            closed.set(true);
        }
    }
}
