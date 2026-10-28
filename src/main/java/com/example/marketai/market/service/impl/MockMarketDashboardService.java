package com.example.marketai.market.service.impl;

import com.example.marketai.market.dto.CandleDTO;
import com.example.marketai.market.dto.CommodityDTO;
import com.example.marketai.market.dto.IndexOverviewDTO;
import com.example.marketai.market.dto.MarketQuoteDTO;
import com.example.marketai.market.service.MarketDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模拟行情看板服务。所有数据基于标的代码确定性生成，保证同一标的在
 * 概览卡片、横条、主图与自选表中保持一致；生产环境可替换为真实行情源实现。
 */
@Slf4j
@Service
public class MockMarketDashboardService implements MarketDashboardService {

    private static final String SOURCE = "Mock Market Data";

    /** 标的元数据：代码 → 名称/分类/区域/货币/单位/基准价 */
    private static final Map<String, Asset> CATALOG = buildCatalog();

    private record Asset(String symbol, String name, String market, String region,
                         String category, String currency, String unit, double base) {
    }

    private static Map<String, Asset> buildCatalog() {
        Map<String, Asset> m = new LinkedHashMap<>();
        // 亚洲指数
        index(m, "000001.SS", "上证指数", "ASIA", "CNY", 3521.36);
        index(m, "399001.SZ", "深证成指", "ASIA", "CNY", 11385.79);
        index(m, "000300.SH", "沪深300", "ASIA", "CNY", 4182.10);
        index(m, "HSI", "恒生指数", "ASIA", "HKD", 24726.88);
        index(m, "N225", "日经225", "ASIA", "JPY", 39038.16);
        // 美国指数
        index(m, "SPX", "标普500", "US", "USD", 5812.44);
        index(m, "IXIC", "纳斯达克", "US", "USD", 18324.17);
        index(m, "DJI", "道琼斯", "US", "USD", 42114.72);
        // 欧洲指数
        index(m, "FTSE", "富时100", "EU", "GBP", 8264.11);
        index(m, "GDAXI", "德国DAX", "EU", "EUR", 19217.06);
        index(m, "FCHI", "法国CAC40", "EU", "EUR", 7549.21);
        index(m, "STOXX50E", "欧洲斯托克50", "EU", "EUR", 4976.13);

        // 贵金属
        commodity(m, "GOLD", "黄金", "PRECIOUS", "USD", "美元/盎司", 2684.50);
        commodity(m, "SILVER", "白银", "PRECIOUS", "USD", "美元/盎司", 31.78);
        commodity(m, "PLATINUM", "铂金", "PRECIOUS", "USD", "美元/盎司", 968.40);
        commodity(m, "PALLADIUM", "钯金", "PRECIOUS", "USD", "美元/盎司", 1015.30);
        commodity(m, "RHODIUM", "铑金", "PRECIOUS", "USD", "美元/盎司", 4640.00);
        // 工业金属
        commodity(m, "COPPER", "铜", "INDUSTRIAL", "USD", "美元/磅", 4.182);
        commodity(m, "ALUMINUM", "铝", "INDUSTRIAL", "USD", "美元/吨", 2623.50);
        commodity(m, "NICKEL", "镍", "INDUSTRIAL", "USD", "美元/吨", 15910.00);
        commodity(m, "ZINC", "锌", "INDUSTRIAL", "USD", "美元/吨", 2941.00);

        // 股票
        stock(m, "AAPL", "苹果", "USD", 232.14);
        stock(m, "MSFT", "微软", "USD", 421.55);
        stock(m, "NVDA", "英伟达", "USD", 138.90);
        stock(m, "TSLA", "特斯拉", "USD", 251.44);
        stock(m, "GOOGL", "谷歌-A", "USD", 178.32);
        stock(m, "AMZN", "亚马逊", "USD", 199.68);
        stock(m, "META", "Meta", "USD", 565.11);
        stock(m, "600519.SH", "贵州茅台", "CNY", 1523.00);
        stock(m, "601318.SH", "中国平安", "CNY", 48.66);
        stock(m, "00700.HK", "腾讯控股", "HKD", 415.80);
        stock(m, "09988.HK", "阿里巴巴", "HKD", 92.35);
        stock(m, "03690.HK", "美团", "HKD", 128.60);

        // 加密
        crypto(m, "BTCUSD", "比特币", "USD", 67480.00);
        crypto(m, "ETHUSD", "以太坊", "USD", 2624.75);
        return m;
    }

    private static void index(Map<String, Asset> m, String symbol, String name, String region, String currency, double base) {
        m.put(symbol, new Asset(symbol, name, "指数", region, null, currency, "", base));
    }

    private static void commodity(Map<String, Asset> m, String symbol, String name, String category, String currency, String unit, double base) {
        m.put(symbol, new Asset(symbol, name, "商品", null, category, currency, unit, base));
    }

    private static void stock(Map<String, Asset> m, String symbol, String name, String currency, double base) {
        m.put(symbol, new Asset(symbol, name, "股票", null, null, currency, "", base));
    }

    private static void crypto(Map<String, Asset> m, String symbol, String name, String currency, double base) {
        m.put(symbol, new Asset(symbol, name, "加密", null, null, currency, "", base));
    }

    @Override
    public List<IndexOverviewDTO> getOverview(String regions) {
        Set<String> regionSet = parseCsv(regions, true);
        return CATALOG.values().stream()
                .filter(a -> "指数".equals(a.market()))
                .filter(a -> regionSet.isEmpty() || regionSet.contains(a.region()))
                .map(a -> {
                    MarketQuoteDTO q = quote(a);
                    IndexOverviewDTO dto = new IndexOverviewDTO();
                    dto.setSymbol(a.symbol());
                    dto.setName(a.name());
                    dto.setRegion(a.region());
                    dto.setCurrency(a.currency());
                    dto.setPrice(q.getPrice());
                    dto.setChange(q.getChange());
                    dto.setChangePercent(q.getChangePercent());
                    dto.setUpdatedAt(q.getUpdatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CommodityDTO> getCommodities(String symbols) {
        List<String> requested = parseCsvList(symbols, true);
        List<Asset> assets;
        if (requested.isEmpty()) {
            assets = CATALOG.values().stream().filter(a -> "商品".equals(a.market())).toList();
        } else {
            // 显式传入 symbols 时按传入顺序返回
            assets = requested.stream()
                    .map(CATALOG::get)
                    .filter(a -> a != null && "商品".equals(a.market()))
                    .toList();
        }
        return assets.stream().map(this::toCommodity).toList();
    }

    private CommodityDTO toCommodity(Asset a) {
        MarketQuoteDTO q = quote(a);
        CommodityDTO dto = new CommodityDTO();
        dto.setSymbol(a.symbol());
        dto.setName(a.name());
        dto.setCategory(a.category());
        dto.setUnit(a.unit());
        dto.setPrice(q.getPrice());
        dto.setChange(q.getChange());
        dto.setChangePercent(q.getChangePercent());
        dto.setUpdatedAt(q.getUpdatedAt());
        return dto;
    }

    private List<String> parseCsvList(String csv, boolean upper) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> upper ? s.toUpperCase() : s)
                .distinct()
                .toList();
    }

    @Override
    public MarketQuoteDTO getQuote(String symbol) {
        return quote(asset(symbol));
    }

    @Override
    public List<CandleDTO> getCandles(String symbol, String interval, Long fromMs, Long toMs, int limit) {
        long step = stepMs(interval);
        long now = System.currentTimeMillis();
        long to = toMs == null ? now : toMs;
        long from = fromMs == null ? to - step * Math.max(limit - 1, 1) : fromMs;
        return buildCandles(asset(symbol), step, from, to, Math.min(Math.max(limit, 1), 2000));
    }

    // ==================== 内部实现 ====================

    private MarketQuoteDTO quote(Asset a) {
        List<CandleDTO> daily = buildCandles(a, 86_400_000L,
                System.currentTimeMillis() - 250L * 86_400_000L, System.currentTimeMillis(), 250);
        CandleDTO last = daily.get(daily.size() - 1);
        CandleDTO prev = daily.get(daily.size() - 2);

        BigDecimal price = last.getClose();
        BigDecimal prevClose = prev.getClose();
        BigDecimal change = price.subtract(prevClose);
        BigDecimal changePercent = prevClose.signum() == 0 ? BigDecimal.ZERO
                : change.divide(prevClose, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal high52 = daily.stream().map(CandleDTO::getHigh).max(BigDecimal::compareTo).orElse(price);
        BigDecimal low52 = daily.stream().map(CandleDTO::getLow).min(BigDecimal::compareTo).orElse(price);

        MarketQuoteDTO dto = new MarketQuoteDTO();
        dto.setSymbol(a.symbol());
        dto.setName(a.name());
        dto.setMarket(a.market());
        dto.setCurrency(a.currency());
        dto.setUnit(a.unit());
        dto.setPrice(price);
        dto.setChange(change);
        dto.setChangePercent(changePercent);
        dto.setOpen(last.getOpen());
        dto.setHigh(last.getHigh());
        dto.setLow(last.getLow());
        dto.setPrevClose(prevClose);
        dto.setHigh52w(high52);
        dto.setLow52w(low52);
        dto.setVolume(last.getVolume());
        dto.setAmount(price.multiply(last.getVolume()).setScale(2, RoundingMode.HALF_UP));
        dto.setSource(SOURCE);
        dto.setUpdatedAt(System.currentTimeMillis());
        return dto;
    }

    private List<CandleDTO> buildCandles(Asset a, long stepMs, long fromMs, long toMs, int limit) {
        long span = Math.max(toMs - fromMs, 1);
        int n = (int) (span / stepMs) + 1;
        n = Math.max(1, Math.min(n, limit));
        long start = toMs - (long) (n - 1) * stepMs;

        Random rnd = new Random(((long) a.symbol().hashCode()) * 2654435761L ^ stepMs);
        double p = a.base() * (0.82 + rnd.nextDouble() * 0.36);

        List<CandleDTO> raw = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double drift = (rnd.nextDouble() - 0.485) * 0.035;
            double open = p;
            double close = open * (1 + drift);
            double high = Math.max(open, close) * (1 + rnd.nextDouble() * 0.006);
            double low = Math.min(open, close) * (1 - rnd.nextDouble() * 0.006);
            double volume = (0.3 + rnd.nextDouble() * 0.7) * (3e8 / Math.max(a.base(), 1));

            CandleDTO c = new CandleDTO();
            c.setTimestamp(start + (long) i * stepMs);
            c.setOpen(bd(open));
            c.setHigh(bd(high));
            c.setLow(bd(low));
            c.setClose(bd(close));
            c.setVolume(bd(volume));
            raw.add(c);
            p = close;
        }

        // 将序列等比缩放，使其终点等于当前价，保证主图与头部价格一致
        double target = currentPrice(a);
        double lastClose = raw.get(raw.size() - 1).getClose().doubleValue();
        double ratio = lastClose == 0 ? 1 : target / lastClose;
        for (CandleDTO c : raw) {
            c.setOpen(bd(c.getOpen().doubleValue() * ratio));
            c.setHigh(bd(c.getHigh().doubleValue() * ratio));
            c.setLow(bd(c.getLow().doubleValue() * ratio));
            c.setClose(bd(c.getClose().doubleValue() * ratio));
        }
        return raw;
    }

    private double currentPrice(Asset a) {
        Random rnd = new Random(((long) a.symbol().hashCode()) * 2654435761L);
        double drift = (rnd.nextDouble() - 0.5) * 0.30;
        return a.base() * (1 + drift);
    }

    private Asset asset(String symbol) {
        String key = symbol == null ? "" : symbol.trim().toUpperCase();
        Asset a = CATALOG.get(key);
        if (a != null) {
            return a;
        }
        double base = 20 + Math.abs(key.hashCode() % 480);
        return new Asset(key, key, "股票", null, null, "USD", "", base);
    }

    private long stepMs(String interval) {
        if (interval == null) {
            return 86_400_000L;
        }
        String it = interval.trim();
        // 月线：支持 "1M"、"1mo"、"month"（大小写敏感地识别 "1M"，避免与分钟 "1m" 冲突）
        if ("1M".equals(it) || "1mo".equalsIgnoreCase(it) || "month".equalsIgnoreCase(it)) {
            return 2_592_000_000L;
        }
        return switch (it.toLowerCase()) {
            case "1m" -> 60_000L;
            case "5m" -> 300_000L;
            case "15m" -> 900_000L;
            case "30m" -> 1_800_000L;
            case "1h", "60m" -> 3_600_000L;
            case "1d", "day" -> 86_400_000L;
            case "1w", "week" -> 604_800_000L;
            default -> 86_400_000L;
        };
    }

    private Set<String> parseCsv(String csv, boolean upper) {
        if (csv == null || csv.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> upper ? s.toUpperCase() : s)
                .collect(Collectors.toSet());
    }

    private BigDecimal bd(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);
    }
}
