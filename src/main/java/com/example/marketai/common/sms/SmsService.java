package com.example.marketai.common.sms;

/**
 * 短信发送抽象，不同通道（阿里云 / 腾讯云 / 模拟）通过实现该接口接入。
 */
public interface SmsService {

    /**
     * 发送验证码短信。
     *
     * @param phone 手机号
     * @param code  验证码
     */
    void sendCode(String phone, String code);
}
