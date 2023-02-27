package com.label.rubblelabeltool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;
import java.net.URL;

import static com.label.rubblelabeltool.util.DllsLoader.loadDll;

@Configuration
@ServletComponentScan
@SpringBootApplication
@MapperScan("com.label.rubblelabeltool.mapper")
public class RubbleLabelToolApplication {
    public static void main(String[] args) {
        loadDll();
        SpringApplication.run(RubbleLabelToolApplication.class, args);
    }

    @Bean
    public MultipartConfigElement getMultipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置文件最大20M
        factory.setMaxFileSize(DataSize.of(20, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(20, DataUnit.MEGABYTES));
        // 设置上传数据总大小为20M
        return factory.createMultipartConfig();
    }

}
