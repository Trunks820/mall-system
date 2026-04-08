# 小程序商城 - 模块设计

## 一、整体模块划分
当前项目采用 Spring Boot 单体分模块架构，按业务领域拆分模块：

- mall-common
- mall-infra
- mall-user
- mall-product
- mall-cart
- mall-order
- mall-pay
- mall-mq
- mall-app
- mall-admin（占位，第一版暂不开发）

## 二、模块职责

### 1. mall-common
公共模块，负责：
- 统一返回对象
- 公共异常
- 状态枚举
- 常量
- 通用分页对象
- 共享工具类（轻量）

### 2. mall-infra
基础设施模块，负责：
- JWT 工具
- 登录拦截器
- 用户上下文
- Redis 配置
- MyBatis 配置
- AOP、日志、防重等基础能力
- 公共配置类

### 3. mall-user
用户模块，负责：
- 微信登录
- 当前用户信息
- 地址管理
- 默认地址设置

### 4. mall-product
商品模块，负责：
- 商品分类
- 商品 SPU
- 商品 SKU
- 商品列表
- 商品详情
- 商品搜索

### 5. mall-cart
购物车模块，负责：
- 加入购物车
- 购物车列表
- 修改数量
- 删除购物车项
- 勾选购物车项
- 结算预览

### 6. mall-order
订单模块，负责：
- 提交订单
- 订单列表
- 订单详情
- 订单取消
- 订单状态查询
- 订单状态流转
- 订单操作日志

### 7. mall-pay
支付模块，负责：
- 创建微信支付
- 支付记录
- 微信支付回调
- 支付结果查询

### 8. mall-mq
MQ 模块，负责：
- 订单超时关闭
- 支付成功异步处理
- MQ 发送与消费逻辑
- 重试 / 死信预留

### 9. mall-app
启动模块，负责：
- Spring Boot 启动类
- application.yml
- 聚合各业务模块
- 启动级配置

### 10. mall-admin
后台模块，当前仅保留占位，不实现后台页面与业务逻辑。

## 三、模块边界规则

### 1. mall-app
只做启动和聚合，不写具体业务逻辑。

### 2. mall-common / mall-infra
所有业务模块可依赖这两个模块。

### 3. 业务模块之间
业务模块尽量不要互相随意调用，优先通过清晰边界配合：
- 用户模块负责用户和地址
- 商品模块负责商品和 SKU
- 购物车模块负责购物车操作
- 订单模块负责下单与状态流转
- 支付模块负责支付与回调
- MQ 模块负责异步消费

## 四、当前建议开发顺序

1. mall-common
2. mall-infra
3. mall-user
4. mall-product
5. mall-cart
6. mall-order
7. mall-pay
8. mall-mq