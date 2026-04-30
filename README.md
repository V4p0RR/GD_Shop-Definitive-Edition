# 贵大电商 (GD Shop)

三端分离电商系统：Spring Boot 后端 + Vue3 管理端 + Vue3 用户端。

## 项目结构

```
shop/
├── GD_shop_backend/      # Java Spring Boot 后端 (端口 8082)
├── GD_shop/              # Vue3 商家管理端 (端口 3001)
├── GD_shop_frontend/     # Vue3 用户端 (端口 3000)
├── db.sql                # MySQL 数据库初始化脚本
└── 使用说明.md            # 详细使用文档
```

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.3.12 + MyBatis-Plus 3.4.3 |
| 数据库 | MySQL + Redis |
| 管理端 UI | Vue 3 + TDesign + Pinia |
| 用户端 UI | Vue 3 + Pinia |
| 构建工具 | Maven (后端) / Vite (前端) |

## 环境要求

- JDK 21+
- MySQL 5.7+
- Redis 7.x（密码 123456，db 1）
- Maven 3.6+
- Node.js 18+ / npm 或 pnpm

## 快速启动

### 1. 初始化数据库

用 MySQL 执行 `db.sql`，创建 `shop_sp` 库及所有表和数据。

### 2. 启动后端

```bash
cd GD_shop_backend
mvn spring-boot:run
```

服务运行在 http://localhost:8082

### 3. 启动管理端

```bash
cd GD_shop
npm install   # 或 pnpm install
npm run dev
```

访问 http://localhost:3001

### 4. 启动用户端

```bash
cd GD_shop_frontend
npm install
npm run dev
```

访问 http://localhost:3000

## 默认账号

| 角色 | 手机号 | 密码 | 权限 |
|------|--------|------|------|
| 超级管理员 | 13600000000 | 123456 | 全部功能 |
| 运营 | 13600000003 | 123456 | 管理商品/订单/秒杀，不可管理用户 |
| 普通用户 | 13600000001 | 123456 | 仅用户端 |

## 角色体系

| 角色值 | 名称 | 可登录端 |
|--------|------|---------|
| 1 | 超级管理员 | 管理端 + 用户端 |
| 2 | 运营 | 管理端（受限）+ 用户端 |
| 0 | 普通用户 | 仅用户端 |

## 主要功能模块

- **用户系统** — 短信/密码登录、角色管理、收货地址、签到
- **商品管理** — SPU/SKU、分类、上下架、多规格
- **购物车** — Redis 购物车，支持未登录操作
- **订单系统** — 下单、发货、退款
- **秒杀** — Redis + Lua 原子秒杀，一人一单
- **数据统计** — 七日销售趋势
- **系统配置** — 店铺信息、支付开关
- **操作日志** — 管理后台操作记录
