package com.example.marketai.ai.controller;

import com.example.marketai.ai.dto.AiAnalysisRequest;
import com.example.marketai.ai.dto.AiAnalysisResponse;
import com.example.marketai.ai.service.AiService;
import com.example.marketai.common.response.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 分析接口。
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/analyze")
    public Result<AiAnalysisResponse> analyze(@Valid @RequestBody AiAnalysisRequest request) {
        return Result.success(aiService.analyze(request));
    }
}
