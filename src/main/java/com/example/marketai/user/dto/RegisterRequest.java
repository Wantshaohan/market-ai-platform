package com.example.marketai.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册请求。
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String code;

    /** 用户名，可选，默认使用手机号 */
    private String username;

    /** 密码，可选，不填则仅支持短信登录 */
    @Size(min = 6, max = 20, message = "密码长度需为 6-20 位")
    private String password;

    private String nickname;
}
