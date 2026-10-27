package com.example.marketai.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建支付订单请求。
 */
@Data
public class CreatePaymentRequest {

    @NotNull(message = "套餐ID不能为空")
    private Long planId;

    /** 支付渠道：alipay / wechat */
    private String channel;
}
