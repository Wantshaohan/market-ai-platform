package com.example.marketai.payment.controller;

import com.example.marketai.common.response.Result;
import com.example.marketai.common.security.SecurityUtils;
import com.example.marketai.payment.dto.CreatePaymentRequest;
import com.example.marketai.payment.entity.PaymentOrder;
import com.example.marketai.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付接口。
 */
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/orders")
    public Result<PaymentOrder> create(@Valid @RequestBody CreatePaymentRequest request) {
        return Result.success(paymentService.createOrder(SecurityUtils.getCurrentUserId(), request));
    }

    @PostMapping("/callback")
    public Result<Void> callback(@RequestParam String orderNo, @RequestParam boolean success) {
        paymentService.handleCallback(orderNo, success);
        return Result.success();
    }
}
