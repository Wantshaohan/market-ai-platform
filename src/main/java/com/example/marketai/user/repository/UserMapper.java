package com.example.marketai.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.marketai.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
