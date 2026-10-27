package com.example.marketai.common.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 开发环境模拟短信服务：验证码直接打印到控制台。
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "market-ai.sms.provider", havingValue = "mock", matchIfMissing = true)
public class MockSmsService implements SmsService {

    @Override
    public void sendCode(String phone, String code) {
        log.info("[MOCK SMS] 向 {} 发送验证码: {}", phone, code);
    }
}
