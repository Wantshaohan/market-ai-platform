package com.example.marketai.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.marketai.common.exception.BusinessException;
import com.example.marketai.common.exception.ErrorCode;
import com.example.marketai.common.response.PageResult;
import com.example.marketai.common.response.Result;
import com.example.marketai.user.dto.UserVO;
import com.example.marketai.user.entity.User;
import com.example.marketai.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理后台接口（仅 ADMIN 角色可访问）。
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserMapper userMapper;

    @GetMapping("/users")
    public Result<PageResult<UserVO>> users(@RequestParam(defaultValue = "1") long pageNum,
                                            @RequestParam(defaultValue = "10") long pageSize) {
        Page<User> page = userMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<User>().orderByDesc(User::getId));
        List<UserVO> records = page.getRecords().stream().map(UserVO::from).toList();
        return Result.success(PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize()));
    }

    @PutMapping("/users/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.setStatus(status);
        userMapper.updateById(user);
        return Result.success();
    }
}
