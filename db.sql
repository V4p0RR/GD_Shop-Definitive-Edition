-- ============================================
-- 贵大电商 数据库初始化
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 一、清理旅无忧旧表（不再需要）
-- ============================================
DROP TABLE IF EXISTS `tb_blog`;
DROP TABLE IF EXISTS `tb_blog_comments`;
DROP TABLE IF EXISTS `tb_follow`;
DROP TABLE IF EXISTS `tb_seckill_voucher`;
DROP TABLE IF EXISTS `tb_shop`;
DROP TABLE IF EXISTS `tb_shop_type`;
DROP TABLE IF EXISTS `tb_sign`;
DROP TABLE IF EXISTS `tb_user_info`;
DROP TABLE IF EXISTS `tb_voucher`;
DROP TABLE IF EXISTS `tb_voucher_order`;

-- ============================================
-- 二、清理电商表（重建）
-- ============================================
DROP TABLE IF EXISTS `tb_seckill_order`;
DROP TABLE IF EXISTS `tb_seckill_activity`;
DROP TABLE IF EXISTS `tb_order_item`;
DROP TABLE IF EXISTS `tb_order`;
DROP TABLE IF EXISTS `tb_sku`;
DROP TABLE IF EXISTS `tb_spu`;
DROP TABLE IF EXISTS `tb_operation_log`;
DROP TABLE IF EXISTS `tb_shop_config`;
DROP TABLE IF EXISTS `tb_category`;

-- ============================================
-- 三、用户表（含角色字段）
-- ============================================
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone` varchar(11) NOT NULL COMMENT '手机号码',
  `password` varchar(128) DEFAULT '' COMMENT '密码，MD5加密',
  `nick_name` varchar(32) DEFAULT '' COMMENT '昵称',
  `icon` varchar(255) DEFAULT '' COMMENT '头像',
  `role` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色 0=普通用户 1=超级管理员 2=运营',
  `default_address` varchar(512) DEFAULT '' COMMENT '默认收货地址',
  `default_receiver` varchar(64) DEFAULT '' COMMENT '默认收货人',
  `default_phone` varchar(11) DEFAULT '' COMMENT '默认收货电话',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniqe_key_phone`(`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Compact;

-- 管理员账号 13600000000 / 123456
INSERT INTO `tb_user` (`id`, `phone`, `password`, `nick_name`, `role`) VALUES
(1, '13600000000', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 1),
(2, '13600000001', 'e10adc3949ba59abbe56e057f20f883e', '张运营', 1),
(3, '13600000002', 'e10adc3949ba59abbe56e057f20f883e', '李运营', 2),
(4, '13600000003', 'e10adc3949ba59abbe56e057f20f883e', '王小明', 0),
(5, '13600000004', 'e10adc3949ba59abbe56e057f20f883e', '赵美丽', 0),
(6, '13600000005', 'e10adc3949ba59abbe56e057f20f883e', '孙大力', 0),
(7, '13600000006', 'e10adc3949ba59abbe56e057f20f883e', '周小芳', 0),
(8, '13600000007', 'e10adc3949ba59abbe56e057f20f883e', '陈志远', 0),
(9, '13600000008', 'e10adc3949ba59abbe56e057f20f883e', '刘小红', 0),
(10, '13600000009', 'e10adc3949ba59abbe56e057f20f883e', '黄大明', 0);

-- ============================================
-- 四、商品分类表
-- ============================================
CREATE TABLE `tb_category` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) DEFAULT NULL COMMENT '分类名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `sort` int(3) UNSIGNED DEFAULT 1 COMMENT '顺序',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Compact;

INSERT INTO `tb_category` (`name`, `icon`, `sort`) VALUES
('手机数码', '/imgs/category/phone.png', 1),
('电脑办公', '/imgs/category/computer.png', 2),
('家用电器', '/imgs/category/home.png', 3),
('服饰鞋包', '/imgs/category/clothes.png', 4),
('食品生鲜', '/imgs/category/food.png', 5),
('图书文具', '/imgs/category/book.png', 6);

-- ============================================
-- 五、商品SPU表
-- ============================================
CREATE TABLE `tb_spu` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128) NOT NULL COMMENT '商品名称',
  `category_id` bigint(20) UNSIGNED NOT NULL COMMENT '分类ID',
  `description` text COMMENT '商品描述',
  `images` varchar(2048) DEFAULT '' COMMENT '商品图片，多张逗号分隔',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态 1=上架 0=下架',
  `sold` int(10) UNSIGNED DEFAULT 0 COMMENT '销量',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Compact;

-- ============================================
-- 六、商品SKU表
-- ============================================
CREATE TABLE `tb_sku` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `spu_id` bigint(20) UNSIGNED NOT NULL COMMENT 'SPU ID',
  `name` varchar(256) DEFAULT '' COMMENT 'SKU名称（规格组合描述）',
  `specs` varchar(512) DEFAULT '' COMMENT '规格参数，如 颜色:红,尺寸:L',
  `price` bigint(20) NOT NULL COMMENT '价格（分）',
  `stock` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '库存',
  `image` varchar(512) DEFAULT '' COMMENT 'SKU图片',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态 1=启用 0=禁用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_spu_id`(`spu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Compact;

-- ============================================
-- 七、订单表
-- ============================================
CREATE TABLE `tb_order` (
  `id` bigint(20) NOT NULL COMMENT '订单ID（雪花算法）',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `total_amount` bigint(20) NOT NULL COMMENT '商品总金额（分）',
  `pay_amount` bigint(20) NOT NULL COMMENT '实付金额（分）',
  `status` tinyint(2) DEFAULT 0 COMMENT '0=待支付 1=已支付 2=已发货 3=已完成 4=已取消 5=退款中 6=已退款',
  `address` varchar(512) DEFAULT '' COMMENT '收货地址',
  `phone` varchar(11) DEFAULT '' COMMENT '收货人电话',
  `receiver` varchar(64) DEFAULT '' COMMENT '收货人姓名',
  `remark` varchar(512) DEFAULT '' COMMENT '用户备注',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `ship_time` timestamp NULL DEFAULT NULL COMMENT '发货时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Compact;

-- ============================================
-- 八、订单商品表
-- ============================================
CREATE TABLE `tb_order_item` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `sku_id` bigint(20) UNSIGNED NOT NULL COMMENT 'SKU ID',
  `spu_id` bigint(20) UNSIGNED NOT NULL COMMENT 'SPU ID',
  `name` varchar(256) DEFAULT '' COMMENT '商品名称',
  `specs` varchar(512) DEFAULT '' COMMENT '规格信息',
  `price` bigint(20) NOT NULL COMMENT '单价（分）',
  `quantity` int(10) NOT NULL DEFAULT 1 COMMENT '购买数量',
  `image` varchar(512) DEFAULT '' COMMENT '商品图片',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Compact;

-- ============================================
-- 九、秒杀活动表
-- ============================================
CREATE TABLE `tb_seckill_activity` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sku_id` bigint(20) UNSIGNED NOT NULL COMMENT 'SKU ID',
  `spu_id` bigint(20) UNSIGNED NOT NULL COMMENT 'SPU ID',
  `spu_name` varchar(128) DEFAULT '' COMMENT '商品名称（冗余）',
  `seckill_price` bigint(20) NOT NULL COMMENT '秒杀价格（分）',
  `stock` int(10) UNSIGNED NOT NULL COMMENT '秒杀库存',
  `begin_time` timestamp NOT NULL DEFAULT '2024-01-01 00:00:00' COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT '2035-12-31 23:59:59' COMMENT '结束时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sku_id`(`sku_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Compact;

-- ============================================
-- 十、秒杀订单表
-- ============================================
CREATE TABLE `tb_seckill_order` (
  `id` bigint(20) NOT NULL COMMENT '订单ID（雪花算法）',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `activity_id` bigint(20) UNSIGNED NOT NULL COMMENT '秒杀活动ID',
  `sku_id` bigint(20) UNSIGNED NOT NULL COMMENT 'SKU ID',
  `seckill_price` bigint(20) NOT NULL COMMENT '秒杀价格（分）',
  `status` tinyint(2) DEFAULT 1 COMMENT '1=已抢到 0=已取消',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_activity`(`user_id`, `activity_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Compact;

-- ============================================
-- 十一、示例商品数据
-- ============================================

-- SPU 1: iPhone 15（手机数码）
INSERT INTO `tb_spu` (`id`, `name`, `category_id`, `description`, `images`, `status`, `sold`) VALUES
(1, 'iPhone 15', 1, 'A16芯片 | 4800万像素 | USB-C接口 | 灵动岛设计', 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01XqBRlc1RbsqWsV2ZR_!!2211686270434.jpg', 1, 56);
INSERT INTO `tb_sku` (`spu_id`, `name`, `specs`, `price`, `stock`, `image`, `status`) VALUES
(1, 'iPhone 15 午夜色 128GB', '颜色:午夜色,容量:128GB', 599900, 120, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01XqBRlc1RbsqWsV2ZR_!!2211686270434.jpg', 1),
(1, 'iPhone 15 星光色 256GB', '颜色:星光色,容量:256GB', 699900, 85, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01XqBRlc1RbsqWsV2ZR_!!2211686270434.jpg', 1),
(1, 'iPhone 15 粉色 512GB', '颜色:粉色,容量:512GB', 899900, 40, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01XqBRlc1RbsqWsV2ZR_!!2211686270434.jpg', 1);

-- SPU 2: MacBook Air M2（电脑办公）
INSERT INTO `tb_spu` (`id`, `name`, `category_id`, `description`, `images`, `status`, `sold`) VALUES
(2, 'MacBook Air M2', 2, 'M2芯片 | 13.6英寸Liquid Retina | 18小时续航 | 轻薄便携', 'https://img.alicdn.com/imgextra/i2/2211686270434/O1CN01bX7RLJ1RbsqZkP89i_!!2211686270434.jpg', 1, 32);
INSERT INTO `tb_sku` (`spu_id`, `name`, `specs`, `price`, `stock`, `image`, `status`) VALUES
(2, 'MacBook Air M2 深空灰 8+256', '颜色:深空灰,内存:8GB,存储:256GB', 799900, 60, 'https://img.alicdn.com/imgextra/i2/2211686270434/O1CN01bX7RLJ1RbsqZkP89i_!!2211686270434.jpg', 1),
(2, 'MacBook Air M2 银色 16+512', '颜色:银色,内存:16GB,存储:512GB', 999900, 35, 'https://img.alicdn.com/imgextra/i2/2211686270434/O1CN01bX7RLJ1RbsqZkP89i_!!2211686270434.jpg', 1);

-- SPU 3: 小米空气净化器（家用电器）
INSERT INTO `tb_spu` (`id`, `name`, `category_id`, `description`, `images`, `status`, `sold`) VALUES
(3, '米家空气净化器 4 Pro', 3, 'CADR 500m³/h | 除甲醛除菌 | 智能联动 | 安静低噪', 'https://img.alicdn.com/imgextra/i1/2211686270434/O1CN01cY7SMk1RbsqVqL1xT_!!2211686270434.jpg', 1, 128);
INSERT INTO `tb_sku` (`spu_id`, `name`, `specs`, `price`, `stock`, `image`, `status`) VALUES
(3, '米家空气净化器 4 Pro 标准版', '版本:标准版', 89900, 200, 'https://img.alicdn.com/imgextra/i1/2211686270434/O1CN01cY7SMk1RbsqVqL1xT_!!2211686270434.jpg', 1),
(3, '米家空气净化器 4 Pro 增强版', '版本:增强版,功能:除甲醛加强', 119900, 150, 'https://img.alicdn.com/imgextra/i1/2211686270434/O1CN01cY7SMk1RbsqVqL1xT_!!2211686270434.jpg', 1);

-- SPU 4: 超轻运动跑鞋（服饰鞋包）
INSERT INTO `tb_spu` (`id`, `name`, `category_id`, `description`, `images`, `status`, `sold`) VALUES
(4, '超轻透气运动跑鞋', 4, '飞织鞋面 | 高弹EVA中底 | 橡胶大底 | 仅重180g', 'https://img.alicdn.com/imgextra/i4/2211686270434/O1CN01dZ8THq1RbsqTuN0eF_!!2211686270434.jpg', 1, 215);
INSERT INTO `tb_sku` (`spu_id`, `name`, `specs`, `price`, `stock`, `image`, `status`) VALUES
(4, '运动跑鞋 黑色 42码', '颜色:黑色,尺码:42', 39900, 88, 'https://img.alicdn.com/imgextra/i4/2211686270434/O1CN01dZ8THq1RbsqTuN0eF_!!2211686270434.jpg', 1),
(4, '运动跑鞋 白色 43码', '颜色:白色,尺码:43', 39900, 96, 'https://img.alicdn.com/imgextra/i4/2211686270434/O1CN01dZ8THq1RbsqTuN0eF_!!2211686270434.jpg', 1),
(4, '运动跑鞋 灰色 41码', '颜色:灰色,尺码:41', 39900, 65, 'https://img.alicdn.com/imgextra/i4/2211686270434/O1CN01dZ8THq1RbsqTuN0eF_!!2211686270434.jpg', 1);

-- SPU 5: 贵州特产大礼包（食品生鲜 — 下架状态）
INSERT INTO `tb_spu` (`id`, `name`, `category_id`, `description`, `images`, `status`, `sold`) VALUES
(5, '遵义辣椒酱', 5, '贵州特产·遵义虾子辣椒酱 200g/瓶 香辣可口', 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01eY9VKv1RbsqOtJ1fN_!!2211686270434.jpg', 0, 89);
INSERT INTO `tb_sku` (`spu_id`, `name`, `specs`, `price`, `stock`, `image`, `status`) VALUES
(5, '遵义辣椒酱 单瓶装', '规格:单瓶200g', 1990, 300, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01eY9VKv1RbsqOtJ1fN_!!2211686270434.jpg', 1),
(5, '遵义辣椒酱 三瓶礼盒装', '规格:3瓶*200g,包装:礼盒', 4990, 150, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01eY9VKv1RbsqOtJ1fN_!!2211686270434.jpg', 1);

-- ============================================
-- 十二、示例订单数据
-- ============================================
INSERT INTO `tb_order` (`id`, `user_id`, `total_amount`, `pay_amount`, `status`, `address`, `phone`, `receiver`, `remark`, `pay_time`, `create_time`) VALUES
(100000000000000001, 1, 599900, 599900, 1, '贵州省贵阳市花溪区贵州大学东校区', '13600000000', '管理员', '请尽快发货', '2026-04-29 14:30:00', '2026-04-29 14:28:00'),
(100000000000000002, 1, 1298800, 1298800, 2, '贵州省贵阳市花溪区贵州大学东校区', '13600000000', '管理员', '', '2026-04-28 10:15:00', '2026-04-28 10:10:00'),
(100000000000000003, 1, 39900, 39900, 0, '贵州省贵阳市南明区新华路12号', '13600000000', '张三', '', NULL, '2026-04-30 09:00:00'),
(100000000000000004, 1, 1688800, 1688800, 3, '贵州省贵阳市云岩区延安中路88号', '13600000000', '李四', '礼品包装', '2026-04-25 16:20:00', '2026-04-25 16:18:00'),
(100000000000000005, 1, 1990, 1990, 5, '贵州省贵阳市观山湖区林城东路100号', '13600000000', '王五', '', '2026-04-22 11:00:00', '2026-04-22 10:58:00');

INSERT INTO `tb_order_item` (`order_id`, `sku_id`, `spu_id`, `name`, `specs`, `price`, `quantity`, `image`) VALUES
(100000000000000001, 1, 1, 'iPhone 15 午夜色 128GB', '颜色:午夜色,容量:128GB', 599900, 1, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01XqBRlc1RbsqWsV2ZR_!!2211686270434.jpg'),
(100000000000000002, 1, 1, 'iPhone 15 午夜色 128GB', '颜色:午夜色,容量:128GB', 599900, 1, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01XqBRlc1RbsqWsV2ZR_!!2211686270434.jpg'),
(100000000000000002, 3, 1, 'iPhone 15 粉色 512GB', '颜色:粉色,容量:512GB', 899900, 1, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01XqBRlc1RbsqWsV2ZR_!!2211686270434.jpg'),
(100000000000000003, 10, 4, '运动跑鞋 黑色 42码', '颜色:黑色,尺码:42', 39900, 1, 'https://img.alicdn.com/imgextra/i4/2211686270434/O1CN01dZ8THq1RbsqTuN0eF_!!2211686270434.jpg'),
(100000000000000004, 2, 1, 'iPhone 15 星光色 256GB', '颜色:星光色,容量:256GB', 699900, 1, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01XqBRlc1RbsqWsV2ZR_!!2211686270434.jpg'),
(100000000000000004, 5, 2, 'MacBook Air M2 银色 16+512', '颜色:银色,内存:16GB,存储:512GB', 999900, 1, 'https://img.alicdn.com/imgextra/i2/2211686270434/O1CN01bX7RLJ1RbsqZkP89i_!!2211686270434.jpg'),
(100000000000000005, 13, 5, '遵义辣椒酱 单瓶装', '规格:单瓶200g', 1990, 1, 'https://img.alicdn.com/imgextra/i3/2211686270434/O1CN01eY9VKv1RbsqOtJ1fN_!!2211686270434.jpg');

-- ============================================
-- 十三、示例秒杀活动
-- ============================================
INSERT INTO `tb_seckill_activity` (`id`, `sku_id`, `spu_id`, `spu_name`, `seckill_price`, `stock`, `begin_time`, `end_time`) VALUES
(1, 11, 4, '运动跑鞋 灰色 41码', 19900, 30, '2026-04-30 00:00:00', '2026-05-07 23:59:59'),
(2, 13, 5, '遵义辣椒酱 单瓶装', 990, 50, '2026-05-01 10:00:00', '2026-05-03 22:00:00');

-- ============================================
-- 十四、店铺配置表
-- ============================================
CREATE TABLE `tb_shop_config` (
  `id` bigint(20) NOT NULL DEFAULT 1,
  `shop_name` varchar(128) DEFAULT '' COMMENT '店铺名称',
  `logo` varchar(512) DEFAULT '' COMMENT 'Logo URL',
  `service_phone` varchar(20) DEFAULT '' COMMENT '客服电话',
  `announcement` varchar(1024) DEFAULT '' COMMENT '店铺公告',
  `wechat_pay` tinyint(1) DEFAULT 1 COMMENT '微信支付开关',
  `alipay` tinyint(1) DEFAULT 1 COMMENT '支付宝开关',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `tb_shop_config` (`id`, `shop_name`, `logo`, `service_phone`, `announcement`, `wechat_pay`, `alipay`) VALUES
(1, '贵大电商', '', '400-888-6666', '欢迎光临贵大电商～全场品质保证，售后无忧！', 1, 1);

-- ============================================
-- 十五、操作日志表
-- ============================================
CREATE TABLE `tb_operation_log` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operator` varchar(64) DEFAULT '' COMMENT '操作人',
  `action` varchar(128) DEFAULT '' COMMENT '操作类型',
  `content` varchar(1024) DEFAULT '' COMMENT '操作内容',
  `type` varchar(16) DEFAULT 'admin' COMMENT 'admin/customer/system',
  `related` varchar(128) DEFAULT '' COMMENT '关联信息',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `tb_operation_log` (`operator`, `action`, `content`, `type`, `related`, `create_time`) VALUES
('管理员', '商品上架', '上架商品 "iPhone 15"，设置3个SKU规格', 'admin', 'SPU ID 1', '2026-04-30 10:30:00'),
('管理员', '商品上架', '上架商品 "MacBook Air M2"', 'admin', 'SPU ID 2', '2026-04-30 10:35:00'),
('管理员', '创建秒杀', '创建秒杀活动 "运动跑鞋 灰色 41码"，秒杀价199元', 'admin', '活动 ID 1', '2026-04-30 11:00:00'),
('张运营', '修改商品', '调整 iPhone 15 午夜色 128GB 库存从100增至120', 'admin', 'SKU ID 1', '2026-04-29 14:20:00'),
('系统', '订单支付', '订单 100000000000000001 支付成功，金额 ¥5999.00', 'system', '订单 100000000000000001', '2026-04-29 14:30:00'),
('系统', '订单支付', '订单 100000000000000002 支付成功，金额 ¥12988.00', 'system', '订单 100000000000000002', '2026-04-28 10:15:00'),
('管理员', '发货操作', '订单 100000000000000002 已发货', 'admin', '订单 100000000000000002', '2026-04-28 15:00:00'),
('系统', '订单完成', '订单 100000000000000004 已完成', 'system', '订单 100000000000000004', '2026-04-26 16:20:00'),
('系统', '库存预警', '商品 "运动跑鞋 黑色 42码" 库存不足10件', 'system', 'SKU ID 10', '2026-04-29 09:00:00'),
('王小明', '申请退款', '订单 100000000000000005 申请退款，原因：买错了', 'customer', '订单 100000000000000005', '2026-04-23 11:30:00'),
('管理员', '处理退款', '同意订单 100000000000000005 退款，金额 ¥19.90', 'admin', '订单 100000000000000005', '2026-04-23 12:00:00'),
('系统', '秒杀预热', '秒杀活动 "遵义辣椒酱 单瓶装" 即将于5月1日开始', 'system', '活动 ID 2', '2026-04-30 12:00:00'),
('管理员', '登录系统', '管理员从 IP 192.168.1.100 登录后台', 'admin', '', '2026-04-30 08:00:00');
SET FOREIGN_KEY_CHECKS = 1;
