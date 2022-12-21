package com.label.rubblelabeltool.util;

import com.label.rubblelabeltool.config.DllsConfigurer;
import com.label.rubblelabeltool.config.ImageConfigurer;
import org.opencv.core.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.net.URL;

public class DllsLoader {

    // 加载javacv库的dll文件
    public static void loadDll() {
//        System.load(DllsConfigurer.openCvDllPath);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
