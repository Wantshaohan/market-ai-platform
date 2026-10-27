package com.example.marketai.membership.controller;

import com.example.marketai.common.response.Result;
import com.example.marketai.common.security.SecurityUtils;
import com.example.marketai.membership.entity.MembershipPlan;
import com.example.marketai.membership.entity.UserMembership;
import com.example.marketai.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会员接口。
 */
@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping("/plans")
    public Result<List<MembershipPlan>> plans() {
        return Result.success(membershipService.listPlans());
    }

    @GetMapping("/my")
    public Result<UserMembership> my() {
        return Result.success(membershipService.getMyMembership(SecurityUtils.getCurrentUserId()));
    }
}
