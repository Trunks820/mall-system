# 项目变更记录

## 说明
本文件用于记录会影响 AI 协作开发的关键变化。
只记录重要变更，不记录普通代码提交。

## 2026-04-08
- 项目采用 Spring Boot 单体分模块架构
- 第一版不使用 Spring Cloud
- mall-app 为唯一启动模块
- 第一版核心模块为：mall-common、mall-infra、mall-user、mall-product、mall-cart、mall-order、mall-pay、mall-mq
- 订单状态固定为：NEW、PAID、SHIPPED、FINISHED、CANCELLED、CLOSED
- 支付状态固定为：UNPAID、PAID、FAIL
- 所有金额统一使用 BigDecimal
- 库存采用 MySQL 条件更新方案