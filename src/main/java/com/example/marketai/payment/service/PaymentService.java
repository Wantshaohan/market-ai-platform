package com.example.marketai.payment.service;

import com.example.marketai.payment.dto.CreatePaymentRequest;
import com.example.marketai.payment.entity.PaymentOrder;

/**
 * 支付服务。
 */
public interface PaymentService {

    PaymentOrder createOrder(Long userId, CreatePaymentRequest request);

    void handleCallback(String orderNo, boolean success);
}
