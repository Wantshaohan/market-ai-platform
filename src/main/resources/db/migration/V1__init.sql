-- =====================================================
-- 初始表结构
-- =====================================================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user
(
    id         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username   VARCHAR(64)  NOT NULL COMMENT '用户名',
    phone      VARCHAR(20)  NOT NULL COMMENT '手机号',
    password   VARCHAR(128) DEFAULT NULL COMMENT '密码(BCrypt)',
    nickname   VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
    avatar     VARCHAR(255) DEFAULT NULL COMMENT '头像',
    status     TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 1正常 0禁用',
    role       VARCHAR(32)  NOT NULL DEFAULT 'USER' COMMENT '角色 USER/ADMIN',
    deleted    TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除 0否 1是',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_phone (phone),
    UNIQUE KEY uk_username (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- 会员套餐表
CREATE TABLE IF NOT EXISTS membership_plan
(
    id         BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    name       VARCHAR(64)    NOT NULL COMMENT '套餐名称',
    price      DECIMAL(10, 2) NOT NULL COMMENT '价格(元)',
    duration   INT            NOT NULL COMMENT '有效时长(天)',
    ai_quota   INT            NOT NULL DEFAULT 0 COMMENT 'AI 分析次数配额',
    features   VARCHAR(512)   DEFAULT NULL COMMENT '权益描述',
    status     TINYINT        NOT NULL DEFAULT 1 COMMENT '状态 1上架 0下架',
    deleted    TINYINT        NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    created_at DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='会员套餐表';

-- 用户会员表
CREATE TABLE IF NOT EXISTS user_membership
(
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id     BIGINT   NOT NULL COMMENT '用户ID',
    plan_id     BIGINT   NOT NULL COMMENT '套餐ID',
    plan_name   VARCHAR(64)  DEFAULT NULL COMMENT '套餐名称(冗余)',
    start_time  DATETIME NOT NULL COMMENT '生效时间',
    end_time    DATETIME NOT NULL COMMENT '失效时间',
    ai_remaining INT     NOT NULL DEFAULT 0 COMMENT '剩余 AI 分析次数',
    status      TINYINT  NOT NULL DEFAULT 1 COMMENT '状态 1有效 0过期',
    deleted     TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户会员表';

-- 支付订单表
CREATE TABLE IF NOT EXISTS payment_order
(
    id          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no    VARCHAR(64)    NOT NULL COMMENT '订单号',
    user_id     BIGINT         NOT NULL COMMENT '用户ID',
    plan_id     BIGINT         NOT NULL COMMENT '套餐ID',
    amount      DECIMAL(10, 2) NOT NULL COMMENT '支付金额(元)',
    status      VARCHAR(16)    NOT NULL DEFAULT 'PENDING' COMMENT '状态 PENDING/SUCCESS/FAILED/CLOSED',
    channel     VARCHAR(16)    DEFAULT NULL COMMENT '支付渠道 alipay/wechat',
    paid_at     DATETIME       DEFAULT NULL COMMENT '支付时间',
    deleted     TINYINT        NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='支付订单表';
