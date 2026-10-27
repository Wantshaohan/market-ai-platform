package com.example.marketai.common.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 阿里云短信实现（占位）。
 * 接入时引入 aliyun-sdk 并实现真实发送逻辑，或通过 HTTP 网关调用。
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "market-ai.sms.provider", havingValue = "aliyun")
public class AliyunSmsService implements SmsService {

    @Override
    public void sendCode(String phone, String code) {
        // TODO: 接入阿里云短信服务（dysmsapi）后实现真实发送
        throw new UnsupportedOperationException("阿里云短信尚未接入，请在 AliyunSmsService 中实现发送逻辑");
    }
}
