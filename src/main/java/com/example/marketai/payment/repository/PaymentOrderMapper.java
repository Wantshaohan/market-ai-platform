package com.example.marketai.payment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.marketai.payment.entity.PaymentOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付订单 Mapper。
 */
@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {
}
