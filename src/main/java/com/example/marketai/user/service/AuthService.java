package com.example.marketai.user.service;

import com.example.marketai.user.dto.LoginRequest;
import com.example.marketai.user.dto.LoginResponse;
import com.example.marketai.user.dto.RegisterRequest;
import com.example.marketai.user.dto.SendSmsRequest;

/**
 * 认证服务。
 */
public interface AuthService {

    void sendSmsCode(SendSmsRequest request);

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
