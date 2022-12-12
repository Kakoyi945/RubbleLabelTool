package com.label.rubblelabeltool.config;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ImageConfigurer {
    /**
     * 建立<num, imgMode字符串>映射
     * 左边为图片类型（如冰雪覆盖图、黑白图等），右边为文件类型的字符串形式
     */
    private static final Map<Integer, String> imgModeMap = new HashMap<>();

    /**
     * 建立<num, path>映射
     * 左边为图片类型（如冰雪覆盖图、黑白图等），右边为文件的上一级路径
     */
    private static final Map<Integer, String>  imgPathMap = new HashMap<>();

    /**
     * 建立<imgType, suffix>映射
     * 左边为MultipartFile获得的图片类型，右边为图片的后缀名
     */
    private static final Map<String, String> imgSuffix = new HashMap<>();

    /**
     * 建立<BufferedImage.TYPE, typeSTr>映射
     * 左边为BufferedImage的图片类型，右边为类型的字符串形式
     */
    private static final Map<Integer, String> imgType = new HashMap<>();

    /**
     * 根据所给的标志返回ImgMode对象
     * @param num
     * @return
     */
    public static ImgMode getImgMode(Integer num) {
        String imgModeStr = imgModeMap.get(num);
        return ImgMode.valueOf(imgModeStr);
    }

    /**
     * 根据bufferedImage的type类型获得字符串形式
     * @param bufferedImageType
     * @return
     */
    public static String getImgTypeStr(Integer bufferedImageType) {
        if(bufferedImageType == BufferedImage.TYPE_INT_RGB || bufferedImageType == BufferedImage.TYPE_3BYTE_BGR) {
            return "jpeg";
        } else {
            return "png";
        }
    }

    /**
     * 根据文件类型设置文件路径，如： {bashPath}/{timeStamp}/raw_rgb/xx.png
     * @param imgMode 图片模式（int）
     * @return 文件路径
     */
    public static String getImgDir(Integer imgMode) {
        String basePath = "D:/project/rubble_detection/data/raw/";
        return basePath + imgPathMap.get(imgMode);
    }

    /**
     * 根据文件类型设置文件路径，如： {bashPath}/{timeStamp}/raw_rgb/xx.png
     * @param imgMode
     * @return
     */
    public static String getLabeledImgDir(Integer imgMode) {
        String basePath = "D:/project/rubble_detection/data/labeled/";
        return basePath + imgPathMap.get(imgMode);
    }

    /**
     * 获取文件的后缀（如.jpg .png）
     * @param type ContentType
     * @return 文件后缀
     */
    public static String getImgSuffix(String type) {
        return imgSuffix.get(type);
    }

    public static final String zipDir = "D:/project/rubble_detection/data/tmp/";

    /**
     * 限制上传文件的最大值
     */
    public static final int IMAGE_MAX_SIZE = 10 * 1024 * 1024;

    /**
     * 限制上传图片类型
     */
    public static final List<String> IMAGE_TYPE = new ArrayList<>();

    /**
     * dll文件位置
     */
    public static final String DLLS = "src/main/resources/dlls/";

    /**
     * 点集的缩放比例
     */
    public static final Integer times = 200;

    /**
     * 图片模式
     */
    public static final Integer RAWRGB = 0;
    public static final Integer RAWICE = 1;
    public static final Integer BINARY = 2;
    public static final Integer ICE = 3;
    public static final Integer HIGHLIGHT = 4;

    @PostConstruct
    public void init() {
        // 初始化 IMAGE_TYPE
        IMAGE_TYPE.add("image/jpeg");
        IMAGE_TYPE.add("image/png");
        // 初始化 imgModeMap
        imgModeMap.put(0, "RAWRGB");
        imgModeMap.put(1, "RAWICE");
        imgModeMap.put(2, "BINARY");
        imgModeMap.put(3, "ICE");
        imgModeMap.put(4, "HIGHLIGHT");
        // 初始化 imgPathMap，只设置文件的上一级文件夹
        imgPathMap.put(0, "raw_rgb/");
        imgPathMap.put(1, "raw_ice/");
        imgPathMap.put(2, "binary/");
        imgPathMap.put(3, "ice/");
        imgPathMap.put(4, "high_light/");
        // 初始化 imgSuffix（如.jpg .png等）
        imgSuffix.put("image/jpeg", ".jpg");
        imgSuffix.put("image/png", ".png");
        // 初始化 imgType（如jpeg png等）
        imgType.put(BufferedImage.TYPE_3BYTE_BGR, "jpeg");
        imgType.put(BufferedImage.TYPE_INT_RGB, "jpeg");
        imgType.put(BufferedImage.TYPE_INT_ARGB, "png");
    }


}
