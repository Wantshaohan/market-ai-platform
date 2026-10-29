package com.example.marketai.market.schedule;

import com.example.marketai.market.service.ForexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 外汇行情定时刷新：每 {@code market-ai.market.forex.fetch-interval-ms}（默认 70s）拉取一次，
 * 并在应用就绪时预热缓存。
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "market-ai.market.forex.enabled", havingValue = "true", matchIfMissing = true)
public class ForexRefreshJob {

    private final ForexService forexService;

    @Scheduled(fixedDelayString = "${market-ai.market.forex.fetch-interval-ms:70000}")
    public void refresh() {
        log.debug("[FOREX] 定时刷新开始");
        forexService.refresh();
    }

    /** 应用就绪后立即预热一次，避免页面首屏等待下一个调度周期 */
    @EventListener(ApplicationReadyEvent.class)
    public void warmUp() {
        try {
            forexService.refresh();
            log.info("[FOREX] 启动预热完成");
        } catch (Exception e) {
            log.warn("[FOREX] 启动预热失败: {}", e.getMessage());
        }
    }
}
