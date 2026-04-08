-- ============================================================
-- 小程序商城 - 第一版建表脚本
-- 数据库：MySQL 5.7+ / 8.0+
-- 字符集：utf8mb4
-- ============================================================

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_user (
    id            BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    open_id       VARCHAR(64)      NOT NULL                COMMENT '微信 openId',
    union_id      VARCHAR(64)      DEFAULT NULL            COMMENT '微信 unionId',
    nick_name     VARCHAR(64)      DEFAULT NULL            COMMENT '昵称',
    avatar_url    VARCHAR(255)     DEFAULT NULL            COMMENT '头像',
    phone         VARCHAR(20)      DEFAULT NULL            COMMENT '手机号',
    status        TINYINT          NOT NULL DEFAULT 1      COMMENT '状态：1正常 0禁用',
    create_time   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_open_id (open_id),
    KEY idx_phone (phone),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -----------------------------------------------------------
-- 2. 用户收货地址表
--    default_user_id 计算列 + 唯一索引：保证每个用户只能有一个有效的默认地址
--    逻辑删除(status=0)后计算列自动变 NULL，释放唯一索引占位
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_user_address (
    id                BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id           BIGINT UNSIGNED  NOT NULL                COMMENT '用户ID',
    receiver_name     VARCHAR(64)      NOT NULL                COMMENT '收货人姓名',
    receiver_phone    VARCHAR(20)      NOT NULL                COMMENT '收货人手机号',
    province          VARCHAR(64)      NOT NULL                COMMENT '省',
    city              VARCHAR(64)      NOT NULL                COMMENT '市',
    district          VARCHAR(64)      NOT NULL                COMMENT '区',
    detail_address    VARCHAR(255)     NOT NULL                COMMENT '详细地址',
    is_default        TINYINT          NOT NULL DEFAULT 0      COMMENT '是否默认地址：1是 0否',
    status            TINYINT          NOT NULL DEFAULT 1      COMMENT '状态：1正常 0删除',
    default_user_id   BIGINT GENERATED ALWAYS AS (CASE WHEN is_default = 1 AND status = 1 THEN user_id ELSE NULL END) STORED,
    create_time       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_default_user_id (default_user_id),
    KEY idx_user_id (user_id),
    KEY idx_user_status (user_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收货地址表';

-- -----------------------------------------------------------
-- 3. 商品分类表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_category (
    id              BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    category_name   VARCHAR(64)      NOT NULL                COMMENT '分类名称',
    sort_num        INT              NOT NULL DEFAULT 0      COMMENT '排序值',
    status          TINYINT          NOT NULL DEFAULT 1      COMMENT '状态：1启用 0禁用',
    create_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_category_name (category_name),
    KEY idx_status_sort (status, sort_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- -----------------------------------------------------------
-- 4. 商品SPU表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_product (
    id              BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    category_id     BIGINT UNSIGNED  NOT NULL                COMMENT '分类ID',
    product_name    VARCHAR(128)     NOT NULL                COMMENT '商品名称',
    product_desc    TEXT             DEFAULT NULL            COMMENT '商品描述',
    main_image      VARCHAR(255)     DEFAULT NULL            COMMENT '主图',
    banner_images   JSON             DEFAULT NULL            COMMENT '轮播图JSON数组，如 ["url1","url2"]',
    status          TINYINT          NOT NULL DEFAULT 1      COMMENT '状态：1上架 0下架',
    sort_num        INT              NOT NULL DEFAULT 0      COMMENT '排序值',
    ext_json        JSON             DEFAULT NULL            COMMENT '扩展字段',
    create_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_category_id (category_id),
    KEY idx_category_status (category_id, status),
    KEY idx_status_sort (status, sort_num),
    KEY idx_status_name (status, product_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SPU表';

-- -----------------------------------------------------------
-- 5. 商品SKU表
--    可售库存 = stock - lock_stock
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_product_sku (
    id              BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    product_id      BIGINT UNSIGNED  NOT NULL                COMMENT '商品ID',
    sku_code        VARCHAR(64)      NOT NULL                COMMENT 'SKU编码',
    sku_name        VARCHAR(128)     NOT NULL                COMMENT 'SKU名称',
    spec_json       JSON             DEFAULT NULL            COMMENT '规格信息，如 {"颜色":"白色","容量":"500ml"}',
    sale_price      DECIMAL(10,2)    NOT NULL                COMMENT '售价',
    origin_price    DECIMAL(10,2)    DEFAULT NULL            COMMENT '原价',
    stock           INT UNSIGNED     NOT NULL DEFAULT 0      COMMENT '总库存',
    lock_stock      INT UNSIGNED     NOT NULL DEFAULT 0      COMMENT '锁定库存',
    sale_count      INT UNSIGNED     NOT NULL DEFAULT 0      COMMENT '销量',
    status          TINYINT          NOT NULL DEFAULT 1      COMMENT '状态：1启用 0禁用',
    create_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sku_code (sku_code),
    KEY idx_product_id (product_id),
    KEY idx_product_status (product_id, status),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- -----------------------------------------------------------
-- 6. 购物车表
--    删除操作直接物理删除，不做逻辑删除
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_cart (
    id              BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id         BIGINT UNSIGNED  NOT NULL                COMMENT '用户ID',
    product_id      BIGINT UNSIGNED  NOT NULL                COMMENT '商品ID',
    sku_id          BIGINT UNSIGNED  NOT NULL                COMMENT 'SKU ID',
    quantity        INT UNSIGNED     NOT NULL DEFAULT 1      COMMENT '购买数量',
    checked         TINYINT          NOT NULL DEFAULT 1      COMMENT '是否勾选：1是 0否',
    create_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_sku (user_id, sku_id),
    KEY idx_user_checked (user_id, checked)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- -----------------------------------------------------------
-- 7. 订单主表
--    收货信息为下单时快照，不依赖地址表外键
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_order (
    id                      BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_no                VARCHAR(64)      NOT NULL                COMMENT '订单号',
    user_id                 BIGINT UNSIGNED  NOT NULL                COMMENT '用户ID',
    total_amount            DECIMAL(10,2)    NOT NULL                COMMENT '订单总金额',
    pay_amount              DECIMAL(10,2)    NOT NULL                COMMENT '实付金额',
    freight_amount          DECIMAL(10,2)    NOT NULL DEFAULT 0.00   COMMENT '运费',
    order_status            VARCHAR(32)      NOT NULL                COMMENT '订单状态：NEW/PAID/SHIPPED/FINISHED/CANCELLED/CLOSED',
    remark                  VARCHAR(255)     DEFAULT NULL            COMMENT '订单备注',
    receiver_name           VARCHAR(64)      NOT NULL                COMMENT '收货人姓名',
    receiver_phone          VARCHAR(20)      NOT NULL                COMMENT '收货人手机号',
    receiver_province       VARCHAR(64)      NOT NULL                COMMENT '收货省份',
    receiver_city           VARCHAR(64)      NOT NULL                COMMENT '收货城市',
    receiver_district       VARCHAR(64)      NOT NULL                COMMENT '收货区县',
    receiver_detail_address VARCHAR(255)     NOT NULL                COMMENT '收货详细地址',
    pay_time                DATETIME         DEFAULT NULL            COMMENT '支付时间',
    ship_time               DATETIME         DEFAULT NULL            COMMENT '发货时间',
    finish_time             DATETIME         DEFAULT NULL            COMMENT '完成时间',
    cancel_time             DATETIME         DEFAULT NULL            COMMENT '取消时间',
    cancel_reason           VARCHAR(255)     DEFAULT NULL            COMMENT '取消原因',
    expire_time             DATETIME         DEFAULT NULL            COMMENT '支付过期时间',
    ext_json                JSON             DEFAULT NULL            COMMENT '扩展字段',
    create_time             DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time             DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_user_status (user_id, order_status),
    KEY idx_user_create_time (user_id, create_time),
    KEY idx_status_create_time (order_status, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- -----------------------------------------------------------
-- 8. 订单明细表
--    商品信息为下单时快照，下单后不受商品修改影响
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_order_item (
    id              BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_id        BIGINT UNSIGNED  NOT NULL                COMMENT '订单ID',
    order_no        VARCHAR(64)      NOT NULL                COMMENT '订单号',
    product_id      BIGINT UNSIGNED  NOT NULL                COMMENT '商品ID',
    sku_id          BIGINT UNSIGNED  NOT NULL                COMMENT 'SKU ID',
    product_name    VARCHAR(128)     NOT NULL                COMMENT '下单时商品名称快照',
    sku_name        VARCHAR(128)     NOT NULL                COMMENT '下单时SKU名称快照',
    main_image      VARCHAR(255)     DEFAULT NULL            COMMENT '下单时主图快照',
    sale_price      DECIMAL(10,2)    NOT NULL                COMMENT '下单时成交单价',
    quantity        INT UNSIGNED     NOT NULL                COMMENT '购买数量',
    total_amount    DECIMAL(10,2)    NOT NULL                COMMENT '行总金额',
    create_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_sku (order_id, sku_id),
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_product_id (product_id),
    KEY idx_sku_id (sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- -----------------------------------------------------------
-- 9. 支付记录表
--    out_trade_no: 提交给微信的商户支付单号，与 order_no 概念分离
--    同一订单可能多次发起支付，order_no 不变但 out_trade_no 不同
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_payment_record (
    id                  BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_no            VARCHAR(64)      NOT NULL                COMMENT '订单号',
    out_trade_no        VARCHAR(64)      NOT NULL                COMMENT '商户支付单号(提交给微信的)',
    pay_channel         VARCHAR(32)      NOT NULL DEFAULT 'WX_MINI' COMMENT '支付渠道',
    pay_status          VARCHAR(32)      NOT NULL DEFAULT 'UNPAID'  COMMENT '支付状态：UNPAID/PAID/FAIL',
    transaction_id      VARCHAR(64)      DEFAULT NULL            COMMENT '第三方支付流水号(微信返回)',
    callback_content    TEXT             DEFAULT NULL            COMMENT '支付回调原始报文',
    pay_amount          DECIMAL(10,2)    NOT NULL                COMMENT '支付金额',
    pay_time            DATETIME         DEFAULT NULL            COMMENT '支付时间',
    create_time         DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP                        COMMENT '创建时间',
    update_time         DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_out_trade_no (out_trade_no),
    UNIQUE KEY uk_transaction_id (transaction_id),
    KEY idx_order_no (order_no),
    KEY idx_pay_status (pay_status),
    KEY idx_order_status (order_no, pay_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- -----------------------------------------------------------
-- 10. 库存流水表
--     stock / lock_stock 语义与 mall_product_sku 保持一致
--     stock = 总库存，lock_stock = 锁定库存，可售 = stock - lock_stock
--     流水只写不改，无 update_time
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_stock_log (
    id                  BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    sku_id              BIGINT UNSIGNED  NOT NULL                COMMENT 'SKU ID',
    biz_type            VARCHAR(32)      NOT NULL                COMMENT '业务类型：ORDER_LOCK/ORDER_CONFIRM/ORDER_ROLLBACK/TIMEOUT_ROLLBACK',
    biz_no              VARCHAR(64)      NOT NULL                COMMENT '业务单号(订单号/退款单号等，与biz_type+sku_id构成幂等键)',
    change_type         VARCHAR(32)      NOT NULL                COMMENT '变更类型：LOCK/UNLOCK/CONFIRM/ROLLBACK',
    change_num          INT UNSIGNED     NOT NULL                COMMENT '变更数量',
    before_stock        INT UNSIGNED     NOT NULL                COMMENT '变更前总库存(对应sku.stock)',
    before_lock_stock   INT UNSIGNED     NOT NULL                COMMENT '变更前锁定库存(对应sku.lock_stock)',
    after_stock         INT UNSIGNED     NOT NULL                COMMENT '变更后总库存(对应sku.stock)',
    after_lock_stock    INT UNSIGNED     NOT NULL                COMMENT '变更后锁定库存(对应sku.lock_stock)',
    remark              VARCHAR(255)     DEFAULT NULL            COMMENT '备注',
    create_time         DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_biz_sku (biz_type, biz_no, sku_id),
    KEY idx_sku_id (sku_id),
    KEY idx_biz_no (biz_no),
    KEY idx_change_type (change_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存流水表';

-- -----------------------------------------------------------
-- 11. 订单操作日志表
--     日志只写不改，无 update_time
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS mall_order_operate_log (
    id              BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_no        VARCHAR(64)      NOT NULL                COMMENT '订单号',
    operate_type    VARCHAR(32)      NOT NULL                COMMENT '操作类型：CREATE/PAY/CANCEL/CLOSE/SHIP/FINISH',
    before_status   VARCHAR(32)      DEFAULT NULL            COMMENT '变更前状态',
    after_status    VARCHAR(32)      DEFAULT NULL            COMMENT '变更后状态',
    operate_desc    VARCHAR(255)     NOT NULL                COMMENT '操作说明',
    operator_id     BIGINT UNSIGNED  DEFAULT NULL            COMMENT '操作人ID',
    operator_name   VARCHAR(64)      DEFAULT NULL            COMMENT '操作人名称',
    create_time     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_order_no (order_no),
    KEY idx_operate_type (operate_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单操作日志表';
