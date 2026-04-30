package com.gdshop.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gdshop.utils.AdminInterceptor;
import com.gdshop.utils.LoginInterceptor;
import com.gdshop.utils.RefreshTokenInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // LoginInterceptor: 需要登录的接口（排除公开访问路径）
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        // 用户认证相关
                        "/user/code",
                        "/user/login",
                        "/user/logout",
                        // 分类
                        "/category/list",
                        "/category/*",
                        // SPU商品浏览
                        "/spu/list",
                        "/spu/*",
                        // SKU浏览
                        "/sku/list",
                        "/sku/list/*",
                        "/sku/*",
                        // 秒杀活动浏览
                        "/seckill/list",
                        "/seckill/activity/*",
                        // 购物车
                        "/cart/**",
                        // 上传
                        "/upload/**")
                .order(1);

        // AdminInterceptor: 需要管理员权限的接口
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns(
                        // 订单管理
                        "/order/ship/*",
                        "/order/admin/**",
                        // SPU写操作（包括admin子路径）
                        "/spu",
                        "/spu/*",
                        "/spu/admin/**",
                        // SKU写操作
                        "/sku",
                        "/sku/*",
                        // 秒杀活动管理
                        "/seckill/activity",
                        "/seckill/activity/*",
                        // 数据统计
                        "/stats/**",
                        // 用户管理
                        "/user/admin/**",
                        "/user/*/status",
                        "/user/*/role",
                        // 店铺配置
                        "/config",
                        "/config/*",
                        // 操作日志
                        "/log/admin/**")
                .order(2);

        // RefreshTokenInterceptor: 刷新token有效期（所有请求）
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
    }
}
