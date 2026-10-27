package com.example.marketai.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.marketai.common.exception.BusinessException;
import com.example.marketai.common.exception.ErrorCode;
import com.example.marketai.membership.entity.MembershipPlan;
import com.example.marketai.membership.repository.MembershipPlanMapper;
import com.example.marketai.payment.dto.CreatePaymentRequest;
import com.example.marketai.payment.entity.PaymentOrder;
import com.example.marketai.payment.repository.PaymentOrderMapper;
import com.example.marketai.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 支付服务实现（骨架）。
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderMapper orderMapper;
    private final MembershipPlanMapper planMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentOrder createOrder(Long userId, CreatePaymentRequest request) {
        MembershipPlan plan = planMapper.selectById(request.getPlanId());
        if (plan == null || plan.getStatus() == null || plan.getStatus() != 1) {
            throw new BusinessException(ErrorCode.MEMBERSHIP_PLAN_NOT_FOUND);
        }

        PaymentOrder order = new PaymentOrder();
        order.setOrderNo("P" + UUID.randomUUID().toString().replace("-", "").substring(0, 24));
        order.setUserId(userId);
        order.setPlanId(plan.getId());
        order.setAmount(plan.getPrice());
        order.setStatus("PENDING");
        order.setChannel(request.getChannel());
        orderMapper.insert(order);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleCallback(String orderNo, boolean success) {
        PaymentOrder order = orderMapper.selectOne(new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getOrderNo, orderNo));
        if (order == null) {
            throw new BusinessException(ErrorCode.PAYMENT_ORDER_NOT_FOUND);
        }

        order.setStatus(success ? "SUCCESS" : "FAILED");
        if (success) {
            order.setPaidAt(LocalDateTime.now());
            // TODO: 支付成功后调用会员服务开通/续期会员
        }
        orderMapper.updateById(order);
    }
}
