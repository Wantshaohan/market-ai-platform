# Market AI Platform（行情 AI 分析平台）

基于 Spring Boot 3 的行情聚合与 AI 分析平台骨架，采用模块化分层架构。

## 技术栈与版本

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.3.5 | 稳定维护线，Java 17 |
| JDK | 17 | LTS |
| MyBatis-Plus | 3.5.7（spring-boot3-starter） | 数据库访问（替换 JPA） |
| MySQL | 8.x（mysql-connector-j） | 关系型数据库 |
| Redis | spring-boot-starter-data-redis | 验证码 / 限流 / 缓存 |
| Flyway | 10.x（含 flyway-mysql） | 数据库迁移 |
| Spring Security + JJWT | 0.12.6 | 无状态 JWT 鉴权 |
| Lombok / Validation / AOP | Boot 托管 | 工具类 |

## 项目结构

```
market-ai-platform/
├── pom.xml
└── src/main/
    ├── java/com/example/marketai/
    │   ├── MarketAiApplication.java
    │   ├── common/
    │   │   ├── config/       # MyBatis-Plus / Redis / CORS / Security 配置
    │   │   ├── exception/    # ErrorCode / BusinessException / 全局异常处理
    │   │   ├── response/     # Result 统一响应 / PageResult 分页响应
    │   │   ├── security/     # JWT 工具 / 过滤器 / 用户主体 / 安全上下文
    │   │   ├── limiter/      # 基于 Redis 的限流（注解 + 组件）
    │   │   └── sms/          # 短信抽象（mock / aliyun）
    │   ├── user/             # 用户模块（短信注册登录）
    │   ├── membership/       # 会员模块
    │   ├── payment/          # 支付模块
    │   ├── market/           # 行情聚合（adapter 隔离第三方行情源）
    │   ├── ai/               # AI 分析（provider 隔离大模型）
    │   └── admin/            # 管理后台
    └── resources/
        ├── application.yml
        └── db/migration/V1__init.sql
```

## 快速开始

### 1. 准备依赖

- JDK 17+
- Maven 3.9+
- MySQL 8（创建数据库 `market_ai`）
- Redis

### 2. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库与 Redis 连接、JWT 密钥：

```yaml
spring:
  datasource:
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

> ⚠️ 生产环境务必替换 `market-ai.jwt.secret`。

### 3. 启动

```bash
cd market-ai-platform
mvn spring-boot:run
```

首次启动时 Flyway 会自动执行 `db/migration` 下的建表脚本。

## 接口示例

默认 `sms.provider=mock`、`ai.provider=mock`，验证码会打印到控制台。

```bash
# 1. 发送验证码（验证码打印在日志中）
curl -X POST http://localhost:8080/api/auth/sms/send \
  -H 'Content-Type: application/json' \
  -d '{"phone":"13800138000","scene":"register"}'

# 2. 注册（用日志中的验证码替换 code）
curl -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"phone":"13800138000","code":"123456","password":"123456"}'

# 3. 密码登录
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"account":"13800138000","password":"123456","loginType":"PASSWORD"}'

# 4. 携带 token 访问受保护接口
curl http://localhost:8080/api/user/me \
  -H 'Authorization: Bearer <token>'

# 5. 行情 / AI（行情公开，AI 需登录）
curl "http://localhost:8080/api/market/quote?symbol=000001"
curl -X POST http://localhost:8080/api/ai/analyze \
  -H 'Content-Type: application/json' -H 'Authorization: Bearer <token>' \
  -d '{"symbol":"000001"}'
```

## 关键设计说明

- **统一响应**：所有接口返回 `Result<T>`，业务错误码集中在 `ErrorCode`。
- **异常处理**：`GlobalExceptionHandler` 统一兜底业务异常、参数校验、鉴权异常。
- **短信注册登录**：验证码存 Redis（5 分钟过期），按手机号 60 秒限流；支持密码登录与短信登录。
- **鉴权**：无状态 JWT，`/api/auth/**` 与 `/api/market/**` 公开，`/api/admin/**` 需 `ADMIN` 角色，其余需登录。
- **扩展点**：
  - 行情源：实现 `MarketDataAdapter` 接口接入真实行情（替换 `MockMarketDataAdapter`）。
  - 大模型：实现 `AiProvider` 接口接入 OpenAI / 通义 / 文心等。
  - 短信：实现 `SmsService` 接口接入阿里云 / 腾讯云。
