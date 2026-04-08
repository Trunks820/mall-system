-- ============================================================
-- 商品模块种子数据（本地开发 & API 联调）
-- 仅涉及 mall_category / mall_product / mall_product_sku
-- 可重复执行（INSERT ... ON DUPLICATE KEY UPDATE 保证幂等）
-- ============================================================
-- ID 分段约定：
--   category : 1001+
--   product  : 2001+
--   sku      : 3001+
-- ============================================================

-- -----------------------------------------------------------
-- 测试场景说明：
--
-- 分类：
--   1001 数码电子  sort=1  status=1
--   1002 生活日用  sort=2  status=1
--   => 测试: GET /api/app/category/list 返回 2 个分类，按 sort 排序
--
-- 商品：
--   2001 Apple AirPods Pro 2 (数码电子) 1个SKU  - 多张轮播图
--   2002 小米充电宝 20000mAh   (数码电子) 2个SKU - 颜色规格
--   2003 膳魔师保温杯 500ml    (生活日用) 2个SKU - 颜色+容量规格
--   => 测试: GET /api/app/product/list 按分类筛选、分页
--   => 测试: GET /api/app/product/detail 返回商品 + SKU 列表
--   => 测试: GET /api/app/product/search?keyword=充电 命中商品2002
--
-- SKU：
--   3001 AirPods Pro 2 白色         ¥1599  库存200
--   3002 小米充电宝 黑色             ¥129   库存500
--   3003 小米充电宝 白色             ¥129   库存300
--   3004 膳魔师保温杯 深蓝色 500ml   ¥199   库存150
--   3005 膳魔师保温杯 樱粉色 500ml   ¥209   库存100
--   => 测试: cart/add, cart/list, cart/settlementPreview
--   => 所有 SKU lock_stock=0, 可售库存 = stock
-- -----------------------------------------------------------

-- -----------------------------------------------------------
-- 1. 商品分类 mall_category
-- -----------------------------------------------------------
INSERT INTO mall_category (id, category_name, sort_num, status)
VALUES
    (1001, '数码电子', 1, 1),
    (1002, '生活日用', 2, 1)
ON DUPLICATE KEY UPDATE
    category_name = VALUES(category_name),
    sort_num      = VALUES(sort_num),
    status        = VALUES(status);

-- -----------------------------------------------------------
-- 2. 商品 SPU mall_product
-- -----------------------------------------------------------
INSERT INTO mall_product (id, category_id, product_name, product_desc, main_image, banner_images, status, sort_num, ext_json)
VALUES
    (2001, 1001,
     'Apple AirPods Pro 2 无线降噪耳机',
     '主动降噪，自适应通透模式，个性化空间音频，MagSafe充电盒，长达6小时聆听时间。',
     'https://img.example.com/product/airpods-pro2-main.jpg',
     '["https://img.example.com/product/airpods-pro2-1.jpg","https://img.example.com/product/airpods-pro2-2.jpg","https://img.example.com/product/airpods-pro2-3.jpg"]',
     1, 1, NULL),

    (2002, 1001,
     '小米充电宝 20000mAh 50W快充版',
     '20000mAh大容量，50W MAX快充，USB-C双向快充，可同时充三台设备，航空级铝合金外壳。',
     'https://img.example.com/product/xiaomi-pb20000-main.jpg',
     '["https://img.example.com/product/xiaomi-pb20000-1.jpg","https://img.example.com/product/xiaomi-pb20000-2.jpg"]',
     1, 2, NULL),

    (2003, 1002,
     '膳魔师不锈钢真空保温杯 500ml',
     '316不锈钢内胆，真空断热技术，保温保冷12小时，食品级PP杯盖，轻量随行。',
     'https://img.example.com/product/thermos-500ml-main.jpg',
     '["https://img.example.com/product/thermos-500ml-1.jpg","https://img.example.com/product/thermos-500ml-2.jpg"]',
     1, 1, NULL)
ON DUPLICATE KEY UPDATE
    category_id   = VALUES(category_id),
    product_name  = VALUES(product_name),
    product_desc  = VALUES(product_desc),
    main_image    = VALUES(main_image),
    banner_images = VALUES(banner_images),
    status        = VALUES(status),
    sort_num      = VALUES(sort_num),
    ext_json      = VALUES(ext_json);

-- -----------------------------------------------------------
-- 3. 商品 SKU mall_product_sku
-- -----------------------------------------------------------
INSERT INTO mall_product_sku (id, product_id, sku_code, sku_name, spec_json, sale_price, origin_price, stock, lock_stock, sale_count, status)
VALUES
    -- AirPods Pro 2：单 SKU
    (3001, 2001, 'AIRPODS-PRO2-WHITE', 'AirPods Pro 2 白色',
     '{"颜色":"白色"}',
     1599.00, 1899.00, 200, 0, 38, 1),

    -- 小米充电宝：2 个颜色 SKU
    (3002, 2002, 'MI-PB20000-BLACK', '小米充电宝 20000mAh 黑色',
     '{"颜色":"黑色"}',
     129.00, 169.00, 500, 0, 126, 1),

    (3003, 2002, 'MI-PB20000-WHITE', '小米充电宝 20000mAh 白色',
     '{"颜色":"白色"}',
     129.00, 169.00, 300, 0, 89, 1),

    -- 膳魔师保温杯：2 个颜色 SKU
    (3004, 2003, 'THERMOS-500-NAVY', '膳魔师保温杯 深蓝色 500ml',
     '{"颜色":"深蓝色","容量":"500ml"}',
     199.00, 259.00, 150, 0, 67, 1),

    (3005, 2003, 'THERMOS-500-PINK', '膳魔师保温杯 樱粉色 500ml',
     '{"颜色":"樱粉色","容量":"500ml"}',
     209.00, 259.00, 100, 0, 45, 1)
ON DUPLICATE KEY UPDATE
    product_id   = VALUES(product_id),
    sku_code     = VALUES(sku_code),
    sku_name     = VALUES(sku_name),
    spec_json    = VALUES(spec_json),
    sale_price   = VALUES(sale_price),
    origin_price = VALUES(origin_price),
    stock        = VALUES(stock),
    lock_stock   = VALUES(lock_stock),
    sale_count   = VALUES(sale_count),
    status       = VALUES(status);
