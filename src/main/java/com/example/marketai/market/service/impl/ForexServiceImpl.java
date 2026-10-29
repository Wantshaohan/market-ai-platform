package com.example.marketai.market.service.impl;

import com.example.marketai.market.config.ForexProperties;
import com.example.marketai.market.dto.ForexKlineDTO;
import com.example.marketai.market.dto.ForexQuoteDTO;
import com.example.marketai.market.dto.ItickKlineResponse;
import com.example.marketai.market.service.ForexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 外汇行情服务实现：RestTemplate 拉取 itick K 线 → Redis 缓存 → 组装快照。
 * 单标的拉取失败仅记日志，不影响其他标的与后续调度。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ForexServiceImpl implements ForexService {

    private static final String CACHE_KEY_PREFIX = "market:kline:";

    private final ForexProperties properties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RestTemplate restTemplate = buildRestTemplate();

    private static RestTemplate buildRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }

    @Override
    public void refresh() {
        for (ForexProperties.Instrument instrument : properties.getInstruments()) {
            try {
                List<ForexKlineDTO> candles = fetchKline(instrument);
                String key = cacheKey(instrument);
                redisTemplate.opsForValue().set(key, candles, Duration.ofSeconds(properties.getCacheTtlSeconds()));
                log.debug("[FOREX] 已缓存 {} 共 {} 根 K 线 -> {}", instrument.getCode(), candles.size(), key);
            } catch (Exception e) {
                log.warn("[FOREX] 拉取 {} 失败: {}", instrument.getCode(), e.getMessage());
            }
        }
    }

    @Override
    public List<ForexQuoteDTO> getQuotes() {
        List<ForexQuoteDTO> result = new ArrayList<>();
        for (ForexProperties.Instrument instrument : properties.getInstruments()) {
            ForexQuoteDTO quote = readQuote(instrument);
            if (quote != null) {
                result.add(quote);
            }
        }
        return result;
    }

    @Override
    public ForexQuoteDTO getQuote(String symbol) {
        ForexProperties.Instrument instrument = findInstrument(symbol);
        return instrument == null ? null : readQuote(instrument);
    }

    // ==================== 内部实现 ====================

    @SuppressWarnings("unchecked")
    private List<ForexKlineDTO> fetchKline(ForexProperties.Instrument instrument) {
        String url = baseUrl() + "/" + instrument.getCategory() + "/kline"
                + "?region=" + instrument.getRegion()
                + "&code=" + instrument.getCode()
                + "&kType=" + properties.getKType()
                + "&limit=" + properties.getLimit();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("token", properties.getToken());

        ResponseEntity<ItickKlineResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), ItickKlineResponse.class);
        ItickKlineResponse body = response.getBody();
        if (body == null || body.getCode() != 0) {
            throw new IllegalStateException("itick 返回异常: code=" + (body == null ? "null" : body.getCode())
                    + ", msg=" + (body == null ? "null" : body.getMsg()));
        }
        return body.getData() == null ? List.of() : body.getData();
    }

    private ForexQuoteDTO readQuote(ForexProperties.Instrument instrument) {
        Object cached = redisTemplate.opsForValue().get(cacheKey(instrument));
        if (!(cached instanceof List<?> list)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<ForexKlineDTO> candles = (List<ForexKlineDTO>) list;
        if (candles.isEmpty()) {
            return null;
        }
        return buildQuote(instrument, candles);
    }

    private ForexQuoteDTO buildQuote(ForexProperties.Instrument instrument, List<ForexKlineDTO> candles) {
        ForexKlineDTO last = candles.get(candles.size() - 1);
        ForexKlineDTO prev = candles.size() >= 2 ? candles.get(candles.size() - 2) : last;

        BigDecimal price = last.getClose();
        BigDecimal change = price.subtract(prev.getClose());
        BigDecimal changePercent = prev.getClose() != null && prev.getClose().signum() != 0
                ? change.divide(prev.getClose(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<BigDecimal> sparkline = candles.stream().map(ForexKlineDTO::getClose).toList();

        ForexQuoteDTO dto = new ForexQuoteDTO();
        dto.setSymbol(instrument.getCode());
        dto.setName(instrument.getName());
        dto.setCategory(instrument.getCategory());
        dto.setPrice(price);
        dto.setChange(change);
        dto.setChangePercent(changePercent);
        dto.setOpen(last.getOpen());
        dto.setHigh(last.getHigh());
        dto.setLow(last.getLow());
        dto.setVolume(last.getVolume());
        dto.setAmount(last.getAmount());
        dto.setUpdatedAt(last.getTimestamp());
        dto.setSparkline(sparkline);
        return dto;
    }

    private ForexProperties.Instrument findInstrument(String symbol) {
        if (symbol == null) {
            return null;
        }
        String code = symbol.trim().toUpperCase();
        return properties.getInstruments().stream()
                .filter(i -> code.equalsIgnoreCase(i.getCode()))
                .findFirst()
                .orElse(null);
    }

    private String cacheKey(ForexProperties.Instrument instrument) {
        return CACHE_KEY_PREFIX + instrument.getCategory() + ":" + instrument.getCode();
    }

    private String baseUrl() {
        String base = properties.getBaseUrl();
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }
}
