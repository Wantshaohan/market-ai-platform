package com.example.marketai.common.limiter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解（基于客户端 IP）。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /** 限流 key 前缀，默认使用方法签名 */
    String key() default "";

    /** 窗口内最大请求次数 */
    int limit() default 10;

    /** 时间窗口（秒） */
    int windowSeconds() default 60;

    /** 触发限流时的提示 */
    String message() default "请求过于频繁，请稍后再试";
}
