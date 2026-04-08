-- ============================================================
-- 用户模块测试数据
-- 适用于本地开发和 API 联调，仅涉及 mall_user / mall_user_address
-- 可重复执行（INSERT ... ON DUPLICATE KEY UPDATE 保证幂等）
-- ============================================================

-- -----------------------------------------------------------
-- 测试场景说明：
--
-- 用户 1 (open_id = mock_test001 / 张三)
--   - 正常用户，已有 4 条地址记录
--   - 地址 1: 北京 - 默认地址 (is_default=1, status=1)
--   - 地址 2: 上海 - 非默认 (is_default=0, status=1)
--   - 地址 3: 广州 - 非默认 (is_default=0, status=1)
--   - 地址 4: 深圳 - 已逻辑删除 (is_default=0, status=0)
--   => 测试: address/list 返回 3 条, setDefault, update, delete
--
-- 用户 2 (open_id = mock_test002 / 李四)
--   - 正常用户，仅有 1 条默认地址
--   => 测试: address/list 返回 1 条, add 新地址
--
-- 用户 3 (open_id = mock_test003 / 王五)
--   - 正常用户，无任何地址
--   => 测试: address/list 返回空, add 首条地址自动设为默认
--
-- 用户 4 (open_id = mock_test004 / 赵六)
--   - 已禁用用户 (status=0)
--   => 测试: wxLogin 登录应被拒绝
--
-- wxLogin 调用方式：
--   POST /api/app/auth/wxLogin  {"code":"test001"}
--   后端 mock 逻辑生成 openId = "mock_test001"，匹配用户 1
-- -----------------------------------------------------------

-- -----------------------------------------------------------
-- 1. 用户表 mall_user
-- -----------------------------------------------------------
INSERT INTO mall_user (id, open_id, union_id, nick_name, avatar_url, phone, status)
VALUES
    (1, 'mock_test001', NULL, '张三', 'https://img.example.com/avatar/zhangsan.png', '13800001001', 1),
    (2, 'mock_test002', NULL, '李四', 'https://img.example.com/avatar/lisi.png',     '13800001002', 1),
    (3, 'mock_test003', NULL, '王五', NULL,                                           NULL,          1),
    (4, 'mock_test004', NULL, '赵六', 'https://img.example.com/avatar/zhaoliu.png',  '13800001004', 0)
ON DUPLICATE KEY UPDATE
    nick_name  = VALUES(nick_name),
    avatar_url = VALUES(avatar_url),
    phone      = VALUES(phone),
    status     = VALUES(status);

-- -----------------------------------------------------------
-- 2. 用户收货地址表 mall_user_address
--    注意: default_user_id 是 GENERATED ALWAYS 计算列，不可手动插入
-- -----------------------------------------------------------

-- 用户 1 的地址：多地址场景（1 默认 + 2 非默认 + 1 已删除）
INSERT INTO mall_user_address (id, user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default, status)
VALUES
    (1, 1, '张三',   '13800001001', '北京市', '北京市', '朝阳区', '建国路93号万达广场A座1801',       1, 1),
    (2, 1, '张三',   '13800001001', '上海市', '上海市', '浦东新区', '陆家嘴环路1000号恒生银行大厦15F', 0, 1),
    (3, 1, '张小三', '13900002002', '广东省', '广州市', '天河区', '天河路385号太古汇北塔2203',        0, 1),
    (4, 1, '张三',   '13800001001', '广东省', '深圳市', '南山区', '科技南十路深圳湾科技生态园6栋',     0, 0)
ON DUPLICATE KEY UPDATE
    user_id        = VALUES(user_id),
    receiver_name  = VALUES(receiver_name),
    receiver_phone = VALUES(receiver_phone),
    province       = VALUES(province),
    city           = VALUES(city),
    district       = VALUES(district),
    detail_address = VALUES(detail_address),
    is_default     = VALUES(is_default),
    status         = VALUES(status);

-- 用户 2 的地址：单地址场景（仅 1 条默认）
INSERT INTO mall_user_address (id, user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default, status)
VALUES
    (5, 2, '李四', '13800001002', '浙江省', '杭州市', '西湖区', '文三路478号华星科技大厦9楼', 1, 1)
ON DUPLICATE KEY UPDATE
    user_id        = VALUES(user_id),
    receiver_name  = VALUES(receiver_name),
    receiver_phone = VALUES(receiver_phone),
    province       = VALUES(province),
    city           = VALUES(city),
    district       = VALUES(district),
    detail_address = VALUES(detail_address),
    is_default     = VALUES(is_default),
    status         = VALUES(status);

-- 用户 3：无地址（不插入任何 address 记录）
-- 用户 4：禁用用户，无地址
