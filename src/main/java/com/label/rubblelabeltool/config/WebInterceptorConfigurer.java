package com.label.rubblelabeltool.config;

import com.label.rubblelabeltool.interceptor.ListInterceptor;
import com.label.rubblelabeltool.interceptor.SubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebInterceptorConfigurer implements WebMvcConfigurer {
    @Autowired
    private ListInterceptor listInterceptor;

    @Autowired
    private SubmitInterceptor submitInterceptor;

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        List<String> patterns = new ArrayList<>();
//        patterns.add("/imgs/directories");
//        // 设置swagger白名单
//        patterns.add("/swagger-ui.html/**");
//        patterns.add("/doc.html");
//        patterns.add("/swagger-resources/**");
//        patterns.add("/webjars/**");
//        patterns.add("/v2/**");
        // 配置list拦截器
        registry.addInterceptor(listInterceptor)
                .addPathPatterns("/imgs/list")
                .order(0);

        // 配置submit拦截器
        registry.addInterceptor(submitInterceptor)
                .addPathPatterns("/imgs/submit")
                .order(10);
    }
}
