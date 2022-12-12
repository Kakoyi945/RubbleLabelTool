package com.label.rubblelabeltool.util;

import com.label.rubblelabeltool.config.ImageConfigurer;
import com.label.rubblelabeltool.entity.ImageInfoEntity;

public class ImagePathUtils {
    public static String getImagePathByMode(ImageInfoEntity imageInfo, Integer imgMode) {
        String path = null;
        if(imgMode.equals(ImageConfigurer.RAWRGB)) {
            path = imageInfo.getRgbPath();
        } else if(imgMode.equals(ImageConfigurer.RAWICE)) {
            path = imageInfo.getRawIcePath();
        } else if(imgMode.equals(ImageConfigurer.BINARY)) {
            path = imageInfo.getBiPath();
        } else if(imgMode.equals(ImageConfigurer.ICE)) {
            path = imageInfo.getIcePath();
        } else if(imgMode.equals(ImageConfigurer.HIGHLIGHT)) {
            path = imageInfo.getHighLightPath();
        }
        return path;
    }
}
