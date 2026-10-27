package com.example.marketai.common.exception;

import lombok.Getter;

/**
 * 统一错误码。200 表示成功，其余为业务/系统错误。
 */
@Getter
public enum ErrorCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    RATE_LIMIT_EXCEEDED(429, "请求过于频繁，请稍后再试"),
    INTERNAL_ERROR(500, "系统内部错误"),

    // 用户 / 认证 1000+
    PHONE_ALREADY_REGISTERED(1001, "手机号已注册"),
    USERNAME_ALREADY_EXISTS(1002, "用户名已存在"),
    USER_NOT_FOUND(1003, "用户不存在"),
    PASSWORD_ERROR(1004, "用户名或密码错误"),
    SMS_CODE_ERROR(1005, "短信验证码错误或已过期"),
    SMS_CODE_SEND_TOO_FREQUENT(1006, "短信发送过于频繁"),
    ACCOUNT_DISABLED(1007, "账号已被禁用"),

    // 会员 / 支付 2000+
    MEMBERSHIP_PLAN_NOT_FOUND(2001, "会员套餐不存在"),
    MEMBERSHIP_NOT_ACTIVE(2002, "会员已过期或未开通"),
    AI_QUOTA_EXHAUSTED(2003, "AI 分析次数已用完"),
    PAYMENT_ORDER_NOT_FOUND(2004, "支付订单不存在"),
    PAYMENT_ERROR(2005, "支付失败"),

    // 行情 / AI 3000+
    MARKET_DATA_ERROR(3001, "行情数据获取失败"),
    AI_PROVIDER_ERROR(3002, "AI 服务调用失败");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
