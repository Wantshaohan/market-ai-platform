package com.example.marketai.membership.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.marketai.membership.entity.MembershipPlan;
import com.example.marketai.membership.entity.UserMembership;
import com.example.marketai.membership.repository.MembershipPlanMapper;
import com.example.marketai.membership.repository.UserMembershipMapper;
import com.example.marketai.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员服务实现。
 */
@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipPlanMapper planMapper;
    private final UserMembershipMapper userMembershipMapper;

    @Override
    public List<MembershipPlan> listPlans() {
        return planMapper.selectList(new LambdaQueryWrapper<MembershipPlan>()
                .eq(MembershipPlan::getStatus, 1)
                .orderByAsc(MembershipPlan::getPrice));
    }

    @Override
    public UserMembership getMyMembership(Long userId) {
        return userMembershipMapper.selectOne(new LambdaQueryWrapper<UserMembership>()
                .eq(UserMembership::getUserId, userId)
                .eq(UserMembership::getStatus, 1)
                .orderByDesc(UserMembership::getEndTime)
                .last("LIMIT 1"));
    }
}
