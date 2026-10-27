package com.example.marketai.membership.service;

import com.example.marketai.membership.entity.MembershipPlan;
import com.example.marketai.membership.entity.UserMembership;

import java.util.List;

/**
 * 会员服务。
 */
public interface MembershipService {

    List<MembershipPlan> listPlans();

    UserMembership getMyMembership(Long userId);
}
