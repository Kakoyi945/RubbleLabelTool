package com.label.rubblelabeltool.interceptor;

import com.label.rubblelabeltool.entity.ImageEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SubmitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 通过HttpServiceRequest对象获取session对象
        ImageEntity image = (ImageEntity)request.getSession().getAttribute("imageCache");
        if(image == null) {
            // 说明不存在image缓存，则打印错误信息
            request.setAttribute("msg", "后端不存在该图片的缓存，自动返回到图片列表界面");
            request.getRequestDispatcher("/imgs/list").forward(request, response);
            return  false;
        }
        return true;
    }
}
