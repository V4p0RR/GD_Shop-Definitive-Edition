package com.gdshop.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();
    // 公开可读的路径：匿名用户 GET 放行，非管理员用户 GET 放行
    private static final String[] PUBLIC_GET_PATTERNS = {
            "/spu/*",
            "/sku/*",
            "/seckill/activity/*",
            "/user/*"
    };
    // 仅超级管理员可操作的路径（运营 role=2 不可访问）
    private static final String[] SUPER_ADMIN_PATTERNS = {
            "/user/admin/**",
            "/user/*/status",
            "/user/*/role"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 匿名用户：允许 GET 公开路径
        if (UserHolder.getUser() == null) {
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                String path = request.getRequestURI();
                for (String pattern : PUBLIC_GET_PATTERNS) {
                    if (MATCHER.match(pattern, path)) {
                        return true;
                    }
                }
            }
            response.setStatus(401);
            return false;
        }
        Integer role = UserHolder.getUser().getRole();
        // 超级管理员 role=1：全部放行
        if (role != null && role == 1) {
            return true;
        }
        // 运营 role=2：放行非用户管理路径
        if (role != null && role == 2) {
            String path = request.getRequestURI();
            for (String pattern : SUPER_ADMIN_PATTERNS) {
                if (MATCHER.match(pattern, path)) {
                    response.setStatus(403);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"success\":false,\"errorMsg\":\"仅超级管理员可操作用户管理\"}");
                    return false;
                }
            }
            return true;
        }
        // 普通用户：允许 GET 公开路径
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String path = request.getRequestURI();
            for (String pattern : PUBLIC_GET_PATTERNS) {
                if (MATCHER.match(pattern, path)) {
                    return true;
                }
            }
        }
        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"errorMsg\":\"无管理员权限\"}");
        return false;
    }
}
