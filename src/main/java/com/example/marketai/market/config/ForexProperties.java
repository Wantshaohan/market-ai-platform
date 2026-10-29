package com.example.marketai.market.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 行情拉取配置（Apollo 就绪）。集中管理 itick 行情源地址、token 与待拉取标的清单。
 * 后续接入 Apollo 配置中心时，把同名 key（market-ai.market.forex.*）迁入命名空间即可。
 */
@Data
@Component
@ConfigurationProperties(prefix = "market-ai.market.forex")
public class ForexProperties {

    /** 是否启用定时拉取 */
    private boolean enabled = true;

    /** itick 行情源基础地址 */
    private String baseUrl = "https://api0.itick.org";

    /** itick 接口鉴权 token */
    private String token = "";

    /** K 线周期（itick 枚举）：1=1分钟 2=5分钟 3=15分钟 4=30分钟 5=1小时 8=1天 9=1周 10=1月 */
    private int kType = 2;

    /** 单标的拉取条数 */
    private int limit = 10;

    /** 拉取间隔（毫秒），默认 70s */
    private long fetchIntervalMs = 70000;

    /** 缓存有效期（秒），应大于拉取间隔以容忍外部源短暂不可用 */
    private long cacheTtlSeconds = 300;

    /** 待拉取标的清单（默认 5 个主流外汇，可扩展 index/stock） */
    private List<Instrument> instruments = new ArrayList<>();

    @Data
    public static class Instrument {
        /** 市场代码，如 GB */
        private String region;

        /** 产品代码，如 EURUSD */
        private String code;

        /** 展示名称 */
        private String name;

        /** 分类：forex / index / stock，用于拼接 itick 路径 {category}/kline */
        private String category = "forex";
    }
}
