package com.helpoldman.interceptor;

import com.helpoldman.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        // 放行登录和注册接口
        String uri = request.getRequestURI();
        if (uri.contains("/auth/login") || uri.contains("/auth/register")) {
            return true;
        }
        
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }
        
        try {
            token = token.substring(7);
            jwtUtil.getUserIdFromToken(token);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}