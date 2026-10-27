package com.example.marketai.user.service;

import com.example.marketai.user.dto.UpdateProfileRequest;
import com.example.marketai.user.dto.UserVO;

/**
 * 用户服务。
 */
public interface UserService {

    UserVO getCurrentUser();

    UserVO updateProfile(UpdateProfileRequest request);
}
