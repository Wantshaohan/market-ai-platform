package com.example.marketai.user.dto;

import com.example.marketai.user.entity.User;
import lombok.Builder;
import lombok.Data;

/**
 * 用户信息视图。
 */
@Data
@Builder
public class UserVO {

    private Long id;
    private String username;
    private String phone;
    private String nickname;
    private String avatar;
    private String role;
    private Integer status;

    public static UserVO from(User user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
