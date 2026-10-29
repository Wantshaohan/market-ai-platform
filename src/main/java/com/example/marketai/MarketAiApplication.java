package com.example.marketai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 行情 AI 分析平台启动类。
 */
@SpringBootApplication
@EnableScheduling
public class MarketAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketAiApplication.class, args);
    }
}
