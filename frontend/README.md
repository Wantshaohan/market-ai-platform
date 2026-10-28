# 行情看板前端（独立目录）

单文件自包含页面 `index.html`，使用原生 HTML/CSS/JS + ECharts（CDN），
对应后端 `/api/v1/**` 行情接口。

## 功能模块

| 模块 | 说明 | 接口 |
|------|------|------|
| MarketNavigation | 顶部导航：指数 / 商品 / 自选 / 发现，主题切换 | — |
| IndexOverview | 顶部全球指数卡片，点击切换主图 | `GET /api/v1/markets/overview?regions=US,EU,ASIA` |
| CommodityTicker | 商品行情横条（自动滚动），点击切换主图 | `GET /api/v1/markets/commodities?symbols=…` |
| MainMarketPanel | 主图：分时 / K线，1D·5D·1M·YTD·6M·1Y·5Y·MAX 切换 | `GET /api/v1/quotes/{symbol}`、`GET /api/v1/market-data/{symbol}/candles` |
| WatchlistTable | 自选表：新增 / 删除 / 拖动排序 / 批量选择 / 行点击 | `GET/POST/DELETE /api/v1/watchlists/default/items` |
| DiscoverPanel | 涨幅榜 / 跌幅榜 / 市场快讯 | 由概览 + 商品数据派生 |

## 运行方式

### 方式一：由后端托管（推荐）

后端 `application.yml` 已配置将本目录作为静态资源，启动后端后访问：

```
http://localhost:8080/
```

### 方式二：独立预览

直接用浏览器打开 `index.html` 即可查看布局（此时接口请求会 404，
页面展示错误态；需配合后端才能看到完整数据）。
