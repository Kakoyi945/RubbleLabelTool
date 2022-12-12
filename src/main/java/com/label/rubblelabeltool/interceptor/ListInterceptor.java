package com.label.rubblelabeltool.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ListInterceptor implements HandlerInterceptor {
    /**
     * 检测全局session对象是否有image数据，如果有则放行，如果没用则重定向到主界面
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器（url + Controller: 映射）
     * @return 如果返回值为true表示放行当前的请求，如果为false则表示拦截当前请求
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 通过HttpServiceRequest对象来获取session对象
        Object dirs = request.getSession().getAttribute("list");
        if(dirs == null) {
            // 说明不存在dirs属性，则重定向到index.html目录
            response.sendRedirect("/imgs/directories");
            return false;
        }
        // 放行
        return true;
    }
}
