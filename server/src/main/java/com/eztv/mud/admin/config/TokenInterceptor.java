package com.eztv.mud.admin.config;

import com.eztv.mud.GameUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-01 10:44
 * 功能: 拦截器
 **/
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getParameter("token");
        String requestURI = request.getRequestURI();
        if(GameUtil.getProp("token").equals(token)||requestURI.contains("/logout")){
            return true;
        }else{
            response.sendRedirect("/logout");
        }
        return false;
    }
 
}
