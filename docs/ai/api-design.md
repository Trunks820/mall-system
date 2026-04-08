# 小程序商城 - 接口设计

## 一、接口约定

### 接口前缀
统一使用：
/api/app

### 鉴权方式
- 除登录接口、支付回调接口、公开商品接口外，其余接口默认需要登录
- 请求头使用 token 或 Authorization: Bearer Token 方案
- 后端通过 JWT + Redis 登录态校验

### 统一返回结构
{
"code": 200,
"msg": "success",
"data": {}
}

## 二、用户模块接口

### 1. 微信登录
- POST /api/app/auth/wxLogin
- 小程序调用 wx.login() 获取 code，传给后端
- 后端调用微信接口换取 openid，并生成平台 token

### 2. 获取当前用户信息
- GET /api/app/user/current

### 3. 地址列表
- GET /api/app/address/list

### 4. 新增地址
- POST /api/app/address/add

### 5. 修改地址
- POST /api/app/address/update

### 6. 删除地址
- POST /api/app/address/delete

### 7. 设置默认地址
- POST /api/app/address/setDefault

## 三、商品模块接口

### 1. 分类列表
- GET /api/app/category/list

### 2. 商品列表
- GET /api/app/product/list
- 参数：
    - categoryId（可选）
    - pageNum
    - pageSize

### 3. 商品详情
- GET /api/app/product/detail
- 参数：
    - productId

### 4. 商品搜索
- GET /api/app/product/search
- 参数：
    - keyword
    - pageNum
    - pageSize

## 四、购物车模块接口

### 1. 加入购物车
- POST /api/app/cart/add
- 若当前用户购物车已存在该 skuId，则累加数量
- 不存在则新增一条

### 2. 购物车列表
- GET /api/app/cart/list

### 3. 修改购物车数量
- POST /api/app/cart/updateQuantity

### 4. 删除购物车项
- POST /api/app/cart/delete

### 5. 勾选购物车项
- POST /api/app/cart/check

### 6. 购物车结算预览
- POST /api/app/cart/settlementPreview

## 五、订单模块接口

### 1. 提交订单
- POST /api/app/order/submit
- 从购物车已选项提交订单
- 处理逻辑：
    - 校验地址
    - 校验购物车项
    - 校验商品状态
    - 校验价格
    - 校验库存
    - 锁库存
    - 创建订单
    - 创建订单明细
    - 发送支付超时关单 MQ

### 2. 订单列表
- GET /api/app/order/list
- 参数：
    - status（可选）
    - pageNum
    - pageSize

### 3. 订单详情
- GET /api/app/order/detail
- 参数：
    - orderNo

### 4. 取消订单
- POST /api/app/order/cancel
- 参数：
    - orderNo
- 第一版只支持待支付订单取消
- 取消后回滚锁库存

### 5. 订单状态查询
- GET /api/app/order/status
- 参数：
    - orderNo

## 六、支付模块接口

### 1. 创建微信支付
- POST /api/app/pay/create
- 参数：
    - orderNo
- 处理逻辑：
    - 校验订单归属
    - 校验订单状态必须为 NEW
    - 调用微信支付下单接口
    - 记录支付记录
    - 返回小程序拉起支付参数

### 2. 微信支付回调
- POST /api/app/pay/callback
- 处理逻辑：
    - 验签
    - 查订单
    - 查支付记录
    - 幂等判断
    - 更新支付状态
    - 更新订单状态
    - 记录订单操作日志
    - 记录库存流水
    - 发送支付成功 MQ

### 3. 支付结果查询
- GET /api/app/pay/result
- 参数：
    - orderNo