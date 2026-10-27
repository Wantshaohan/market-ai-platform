package com.example.marketai.user.dto;

import lombok.Data;

/**
 * 更新个人资料请求。
 */
@Data
public class UpdateProfileRequest {

    private String nickname;
    private String avatar;
}
