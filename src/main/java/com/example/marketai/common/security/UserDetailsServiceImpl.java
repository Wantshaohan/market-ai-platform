package com.example.marketai.common.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.marketai.user.entity.User;
import com.example.marketai.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户详情加载：支持按用户名或手机号查询。
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, account)
                .or()
                .eq(User::getPhone, account));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + account);
        }
        return new UserPrincipal(user);
    }

    public UserPrincipal loadUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + id);
        }
        return new UserPrincipal(user);
    }
}
