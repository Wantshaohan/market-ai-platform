package com.example.marketai.user.service.impl;

import com.example.marketai.common.exception.BusinessException;
import com.example.marketai.common.exception.ErrorCode;
import com.example.marketai.common.security.SecurityUtils;
import com.example.marketai.user.dto.UpdateProfileRequest;
import com.example.marketai.user.dto.UserVO;
import com.example.marketai.user.entity.User;
import com.example.marketai.user.repository.UserMapper;
import com.example.marketai.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现。
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserVO getCurrentUser() {
        return UserVO.from(getCurrentUserEntity());
    }

    @Override
    public UserVO updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUserEntity();
        if (StringUtils.hasText(request.getNickname())) {
            user.setNickname(request.getNickname());
        }
        if (StringUtils.hasText(request.getAvatar())) {
            user.setAvatar(request.getAvatar());
        }
        userMapper.updateById(user);
        return UserVO.from(userMapper.selectById(user.getId()));
    }

    private User getCurrentUserEntity() {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }
}
