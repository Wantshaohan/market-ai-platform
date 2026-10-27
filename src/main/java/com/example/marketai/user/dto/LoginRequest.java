package com.example.marketai.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求。
 */
@Data
public class LoginRequest {

    /** 账号：用户名或手机号 */
    @NotBlank(message = "账号不能为空")
    private String account;

    /** 密码（密码登录时必填） */
    private String password;

    /** 短信验证码（短信登录时必填） */
    private String code;

    /** 登录方式：PASSWORD / SMS */
    @NotBlank(message = "登录方式不能为空")
    private String loginType;
}
