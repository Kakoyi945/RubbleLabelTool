package com.label.rubblelabeltool.util;

import com.label.rubblelabeltool.config.ImageConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.net.URL;

public class DllsLoader {
    // 加载javacv库的dll文件
    public static void loadDll() {
        System.setProperty("java.awt.headless", "false");
        System.out.println(System.getProperty("java.library.path"));
//        ClassPathResource classPathResource = new ClassPathResource("dlls/opencv_java460.dll");
//        String path = classPathResource.getPath();
        URL url = ClassLoader.getSystemResource("dlls/opencv_java460.dll");
        String path = url.getPath();
        System.out.println(path);
        System.load(path);
    }
}
