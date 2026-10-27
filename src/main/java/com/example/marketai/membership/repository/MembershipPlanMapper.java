package com.example.marketai.membership.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.marketai.membership.entity.MembershipPlan;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员套餐 Mapper。
 */
@Mapper
public interface MembershipPlanMapper extends BaseMapper<MembershipPlan> {
}
