package com.label.rubblelabeltool.config;

import com.label.rubblelabeltool.util.ServerRealPathUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfigurer implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(ImageConfigurer.zipUrl+"**")
                .addResourceLocations("file:"+ImageConfigurer.zipDir);
    }
}
