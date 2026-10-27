package com.example.marketai.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.marketai.common.exception.BusinessException;
import com.example.marketai.common.exception.ErrorCode;
import com.example.marketai.common.limiter.RateLimiter;
import com.example.marketai.common.security.JwtUtil;
import com.example.marketai.common.security.UserPrincipal;
import com.example.marketai.common.sms.SmsService;
import com.example.marketai.user.dto.LoginRequest;
import com.example.marketai.user.dto.LoginResponse;
import com.example.marketai.user.dto.RegisterRequest;
import com.example.marketai.user.dto.SendSmsRequest;
import com.example.marketai.user.dto.UserVO;
import com.example.marketai.user.entity.User;
import com.example.marketai.user.repository.UserMapper;
import com.example.marketai.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Duration;

/**
 * 认证服务实现：短信验证码、注册、密码/短信登录。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String SMS_CODE_KEY = "sms:code:";
    private static final String SMS_LIMIT_KEY = "sms:limit:";

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final StringRedisTemplate stringRedisTemplate;
    private final SmsService smsService;
    private final RateLimiter rateLimiter;
    private final JwtUtil jwtUtil;

    @Value("${market-ai.sms.code-ttl:300}")
    private long codeTtl;

    private final SecureRandom random = new SecureRandom();

    @Override
    public void sendSmsCode(SendSmsRequest request) {
        String phone = request.getPhone();

        // 按手机号限流：60 秒内仅允许发送一次
        if (!rateLimiter.tryAcquire(SMS_LIMIT_KEY + phone, 1, 60)) {
            throw new BusinessException(ErrorCode.SMS_CODE_SEND_TOO_FREQUENT);
        }

        String code = generateCode();
        stringRedisTemplate.opsForValue().set(SMS_CODE_KEY + phone, code, Duration.ofSeconds(codeTtl));
        smsService.sendCode(phone, code);
        log.info("验证码已发送，phone={}, scene={}", phone, request.getScene());
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        String phone = request.getPhone();
        verifyCode(phone, request.getCode());

        // 手机号唯一性校验
        Long phoneCount = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (phoneCount != null && phoneCount > 0) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_REGISTERED);
        }

        // 用户名默认使用手机号
        String username = StringUtils.hasText(request.getUsername()) ? request.getUsername() : phone;
        Long usernameCount = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (usernameCount != null && usernameCount > 0) {
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(StringUtils.hasText(request.getPassword())
                ? passwordEncoder.encode(request.getPassword())
                : null);
        user.setNickname(StringUtils.hasText(request.getNickname())
                ? request.getNickname()
                : "用户" + phone.substring(phone.length() - 4));
        user.setStatus(1);
        user.setRole("USER");
        userMapper.insert(user);

        return buildLoginResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if ("SMS".equalsIgnoreCase(request.getLoginType())) {
            return loginBySms(request);
        }
        return loginByPassword(request);
    }

    private LoginResponse loginBySms(LoginRequest request) {
        String phone = request.getAccount();
        verifyCode(phone, request.getCode());

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        return buildLoginResponse(user);
    }

    private LoginResponse loginByPassword(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword()));
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return buildLoginResponse(principal.getUser());
        } catch (DisabledException e) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        } catch (BadCredentialsException e) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
    }

    private LoginResponse buildLoginResponse(User user) {
        String token = jwtUtil.generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .user(UserVO.from(user))
                .build();
    }

    private void verifyCode(String phone, String code) {
        String cached = stringRedisTemplate.opsForValue().get(SMS_CODE_KEY + phone);
        if (!StringUtils.hasText(cached) || !cached.equals(code)) {
            throw new BusinessException(ErrorCode.SMS_CODE_ERROR);
        }
        stringRedisTemplate.delete(SMS_CODE_KEY + phone);
    }

    private String generateCode() {
        return String.valueOf(100000 + random.nextInt(900000));
    }
}
