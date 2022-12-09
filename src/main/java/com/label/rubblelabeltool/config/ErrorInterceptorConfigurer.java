package com.label.rubblelabeltool.config;

import com.label.rubblelabeltool.interceptor.MainInterpetor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ErrorInterceptorConfigurer implements WebMvcConfigurer {

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义的拦截器对象
        HandlerInterceptor interceptor = new MainInterpetor();

        // 配置白名单，存放在一个List中
        List<String> patterns = new ArrayList<>();
        patterns.add("/index.html");
        patterns.add("/error/error.html");
        patterns.add("/imgs/**");
        patterns.add("/js/**");
        // patterns.add("/upload");
        // 配置swagger白名单
        patterns.add("/swagger-ui.html/**");
        patterns.add("/doc.html");
        patterns.add("/swagger-resources/**");
        patterns.add("/webjars/**");
        patterns.add("/v2/**");

        // 完成拦截器的注册
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);
    }
}
