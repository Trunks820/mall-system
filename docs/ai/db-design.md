# 小程序商城 - 数据库设计

## 一、表清单

### 1. mall_user
用户表，存储微信用户身份与基础信息。

建议核心字段：
- id
- open_id
- union_id
- nickname
- avatar_url
- phone
- status
- create_time
- update_time

### 2. mall_user_address
用户收货地址表。

建议核心字段：
- id
- user_id
- receiver_name
- receiver_phone
- province
- city
- district
- detail_address
- is_default
- create_time
- update_time

### 3. mall_category
商品分类表。

建议核心字段：
- id
- name
- sort
- status
- create_time
- update_time

### 4. mall_product
商品主表（SPU）。

建议核心字段：
- id
- category_id
- product_name
- product_sub_title
- main_image
- album_images
- status
- sale_count
- ext_json
- create_time
- update_time

### 5. mall_product_sku
商品 SKU 表，用于价格和库存管理。

建议核心字段：
- id
- product_id
- sku_code
- sku_name
- sale_price
- original_price
- stock
- lock_stock
- sale_count
- status
- create_time
- update_time

### 6. mall_cart
购物车表。

建议核心字段：
- id
- user_id
- product_id
- sku_id
- quantity
- checked
- create_time
- update_time

### 7. mall_order
订单主表。

建议核心字段：
- id
- order_no
- user_id
- status
- receiver_name
- receiver_phone
- receiver_province
- receiver_city
- receiver_district
- receiver_detail_address
- total_amount
- freight_amount
- discount_amount
- pay_amount
- pay_status
- pay_time
- close_time
- ext_json
- create_time
- update_time

### 8. mall_order_item
订单明细表。

建议核心字段：
- id
- order_id
- order_no
- product_id
- sku_id
- product_name
- sku_name
- product_image
- sale_price
- quantity
- total_amount
- create_time
- update_time

### 9. mall_payment_record
支付记录表。

建议核心字段：
- id
- order_id
- order_no
- pay_status
- pay_channel
- pay_amount
- third_trade_no
- callback_content
- pay_time
- create_time
- update_time

### 10. mall_stock_log
库存流水表。

建议核心字段：
- id
- order_no
- sku_id
- biz_type
- quantity
- remark
- create_time

### 11. mall_order_operate_log
订单操作日志表。

建议核心字段：
- id
- order_id
- order_no
- operate_type
- operator_id
- operator_name
- remark
- create_time

## 二、表关系

1. mall_user 和 mall_user_address：一对多
2. mall_category 和 mall_product：一对多
3. mall_product 和 mall_product_sku：一对多
4. mall_user 和 mall_cart：一对多
5. mall_user 和 mall_order：一对多
6. mall_order 和 mall_order_item：一对多
7. mall_order 和 mall_payment_record：一对一或一对多
8. mall_product_sku 和 mall_stock_log：一对多
9. mall_order 和 mall_order_operate_log：一对多

## 三、订单状态设计

订单状态只允许使用以下枚举值：

- NEW：待支付
- PAID：已支付
- SHIPPED：已发货
- FINISHED：已完成
- CANCELLED：已取消
- CLOSED：超时关闭

### 状态流转规则
- NEW -> PAID / CANCELLED / CLOSED
- PAID -> SHIPPED
- SHIPPED -> FINISHED
- FINISHED / CANCELLED / CLOSED 为终态

## 四、支付状态设计

支付状态只允许使用以下枚举值：

- UNPAID：未支付
- PAID：已支付
- FAIL：支付失败

## 五、库存业务类型设计

库存流水业务类型：

- ORDER_LOCK：下单锁库存
- ORDER_CONFIRM：支付成功确认扣减
- ORDER_ROLLBACK：用户取消回滚库存
- TIMEOUT_ROLLBACK：超时关单回滚库存

## 六、订单操作类型设计

订单操作日志类型：

- CREATE：创建订单
- PAY：支付
- CANCEL：用户取消
- CLOSE：超时关闭
- SHIP：发货
- FINISH：确认完成

## 七、金额字段规则

### 金额统一要求
1. 所有金额字段统一使用 DECIMAL(10,2)
2. Java 侧统一使用 BigDecimal
3. 禁止使用 float 或 double
4. 所有金额计算必须由后端完成，不信任前端金额

### 金额公式
- 单行金额 = sku.sale_price * quantity
- 商品总金额 = 所有明细金额求和
- 订单总金额(total_amount) = 商品总金额 + 运费
- 实付金额(pay_amount) = total_amount - 优惠金额

第一版优惠金额固定为 0。

## 八、库存控制规则

第一版采用 MySQL 条件更新方案控制库存，通过 stock 和 lock_stock 字段实现。

### 锁库存 SQL
    UPDATE mall_product_sku
    SET lock_stock = lock_stock + #{quantity}
    WHERE id = #{skuId}
      AND (stock - lock_stock) >= #{quantity};

### 解锁库存 SQL
    UPDATE mall_product_sku
    SET lock_stock = lock_stock - #{quantity}
    WHERE id = #{skuId}
      AND lock_stock >= #{quantity};

### 支付成功确认扣减 SQL
    UPDATE mall_product_sku
    SET stock = stock - #{quantity},
        lock_stock = lock_stock - #{quantity},
        sale_count = sale_count + #{quantity}
    WHERE id = #{skuId}
      AND stock >= #{quantity}
      AND lock_stock >= #{quantity};

### 约束
- 每次更新后必须检查影响行数
- 影响行数为 0，说明库存不足或状态异常，必须抛异常回滚
- 多 SKU 订单中任一 SKU 失败，整体事务回滚

## 九、索引建议

### mall_user
- uk_open_id(open_id)

### mall_user_address
- idx_user_id(user_id)

### mall_category
- idx_status_sort(status, sort)

### mall_product
- idx_category_status(category_id, status)
- idx_status(status)

### mall_product_sku
- idx_product_id(product_id)
- uk_sku_code(sku_code)

### mall_cart
- uk_user_sku(user_id, sku_id)

### mall_order
- uk_order_no(order_no)
- idx_user_status(user_id, status)
- idx_create_time(create_time)

### mall_order_item
- idx_order_no(order_no)
- idx_order_id(order_id)

### mall_payment_record
- idx_order_no(order_no)
- idx_pay_status(pay_status)

### mall_stock_log
- idx_order_no(order_no)
- idx_sku_id(sku_id)

### mall_order_operate_log
- idx_order_no(order_no)
- idx_order_id(order_id)

## 十、扩展字段
为了后续扩展，建议预留：
- mall_order.ext_json
- mall_product.ext_json