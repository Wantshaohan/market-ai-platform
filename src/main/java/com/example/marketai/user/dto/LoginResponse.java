package com.example.marketai.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 登录响应。
 */
@Data
@Builder
public class LoginResponse {

    private String token;
    private UserVO user;
}
