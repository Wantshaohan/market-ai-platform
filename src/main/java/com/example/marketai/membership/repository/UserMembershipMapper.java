package com.example.marketai.membership.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.marketai.membership.entity.UserMembership;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户会员 Mapper。
 */
@Mapper
public interface UserMembershipMapper extends BaseMapper<UserMembership> {
}
