package com.example.marketai.membership.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户会员实体。
 */
@Data
@TableName("user_membership")
public class UserMembership {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long planId;

    private String planName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer aiRemaining;

    /** 状态：1 有效，0 过期 */
    private Integer status;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
