package com.example.marketai.user.controller;

import com.example.marketai.common.limiter.RateLimit;
import com.example.marketai.common.response.Result;
import com.example.marketai.user.dto.LoginRequest;
import com.example.marketai.user.dto.LoginResponse;
import com.example.marketai.user.dto.RegisterRequest;
import com.example.marketai.user.dto.SendSmsRequest;
import com.example.marketai.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口：短信发送、注册、登录。
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sms/send")
    @RateLimit(key = "auth:sms:send", limit = 5, windowSeconds = 60)
    public Result<Void> sendSms(@Valid @RequestBody SendSmsRequest request) {
        authService.sendSmsCode(request);
        return Result.success();
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }
}
