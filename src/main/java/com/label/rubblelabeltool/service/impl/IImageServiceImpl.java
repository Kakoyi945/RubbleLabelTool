package com.label.rubblelabeltool.service.impl;

import com.label.rubblelabeltool.config.ImageConfigurer;
import com.label.rubblelabeltool.config.ImgMode;
import com.label.rubblelabeltool.controller.ex.ImageInfoErrorException;
import com.label.rubblelabeltool.entity.ImageEntity;
import com.label.rubblelabeltool.entity.ImageInfoEntity;
import com.label.rubblelabeltool.entity.PointsEntity;
import com.label.rubblelabeltool.mapper.ImageInfoMapper;
import com.label.rubblelabeltool.service.IImageService;
import com.label.rubblelabeltool.service.ex.*;
import com.label.rubblelabeltool.util.CvTools;
import com.label.rubblelabeltool.util.PageUtils;
import org.opencv.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IImageServiceImpl implements IImageService {
    @Autowired(required = false)
    ImageInfoMapper imageInfoMapper;

    @Override
    public Integer uploadImage(String imageName, Date uploadTime, Integer imgModeInt, String path, Double size, String type) {
        ImgMode imgMode = ImageConfigurer.getImgMode(imgModeInt);
        if(imgMode != ImgMode.RAWICE && imgMode != ImgMode.RAWRGB) {
            throw new ImageModeUnsatisfiedException("所传送的图片不是规定的类型，需求类型为原始的ice或者rgb，实际类型为" + imgMode);
        }
        // 补充数据
        ImageInfoEntity imageInfo = new ImageInfoEntity();
        imageInfo.setEditTime(uploadTime);
        imageInfo.setFileName(imageName);
        imageInfo.setSize(size);
        imageInfo.setType(type);
        if(imgMode == ImgMode.RAWRGB) {
            imageInfo.setRawRgbPath(path);
        } else {
            imageInfo.setRawIcePath(path);
        }
        return imageInfoMapper.insert(imageInfo);
    }

    @Override
    public ImageInfoEntity getImageInfoById(Integer id) {
        ImageInfoEntity imageInfo = imageInfoMapper.queryById(id);
        if(imageInfo == null) {
            throw new ImageInfoEmptyException("查询的图片不存在");
        }
        return imageInfo;
    }

    @Override
    public ImageEntity changeImage(ImageEntity image, List<PointsEntity> ptes) {
        // biImg
        Mat biImg = image.getBiImg();
        // 如果第一次标注，则biImg为空，新建biImg对象
        if(biImg == null) {
            biImg = new Mat(image.getRawRgbImg().size(), CvType.CV_8UC1, new Scalar(0));
            image.setBiImg(biImg);
        }
        // iceImg
        Mat iceImg = image.getIceImg();
        if(iceImg == null) {
            iceImg = image.getRawIceImg();
        }
        // highLightImg
        Mat highLightImg = image.getHighLightImg();
        if(highLightImg == null) {
            highLightImg = image.getRawRgbImg();
        }
        // 如果传入的点集为空，则填充全部图像并返回
        if(ptes.size() == 0) {
            image.setBiImg(biImg);
            image.setIceImg(iceImg);
            image.setHighLightImg(highLightImg);
            return image;
        }
        // 计算缩放比例
        CvTools.calScaler(image.getWidth(), image.getHeight(), ImageConfigurer.times);
        // 先将点集转化为List<MatOfPoint>形式
        List<MatOfPoint> mops = CvTools.changePtsToMops(ptes);
        // 填充点集圈出来的区域
        CvTools.fillArea(biImg, mops, ImgMode.BINARY);
        image.setBiImg(biImg);
        // 由于一开始以为冰雪覆盖图也需要标注，所以添加了iceImg的字段，为了省事，直接把标注冰雪覆盖图代码注释了
        // CvTools.fillArea(iceImg, mops, ImgMode.ICE);
        image.setIceImg(iceImg);
        CvTools.fillArea(highLightImg, mops, ImgMode.HIGHLIGHT);
        image.setHighLightImg(highLightImg);
        return image;
    }

    @Override
    public Integer storeImageInfo(Integer id, String biPath, String icePath, String highLightPath, Date labeledTime) {
        if(biPath == null || icePath == null || highLightPath == null || labeledTime == null) {
            throw new ImageInfoErrorException("图片信息存在缺失");
        }
        return imageInfoMapper.updateLabeledImagesInfoById(id, biPath, icePath, highLightPath, labeledTime);
    }


    @Override
    public ImageInfoEntity getImageInfoByFileName(String fileName) {
        return imageInfoMapper.queryImagInfoByImageName(fileName);
    }

    @Override
    public Integer updateImage(ImageInfoEntity imageInfo, String path, Integer imgMode) {
        Integer id = imageInfo.getId();
        Integer cols = 0;
        if(ImageConfigurer.getImgMode(imgMode) == ImgMode.RAWRGB && imageInfo.getRawRgbPath() == null) {
            cols = imageInfoMapper.updateRawRgbPathById(id, path);
        } else if(ImageConfigurer.getImgMode(imgMode) == ImgMode.RAWICE && imageInfo.getRawIcePath() == null) {
            cols = imageInfoMapper.updateRawIcePathById(id, path);
        } else {
            if(imageInfo.getRawRgbPath() != null && imageInfo.getRawIcePath() != null) {
                throw new ImageDuplicatedException("该图片的初始路径（rgb和ice）信息已经存在");
            }
        }
        return cols;
    }

    @Override
    public List<ImageInfoEntity> getImageInfos(PageUtils pageUtils) {
        List<ImageInfoEntity> imageInfos = imageInfoMapper.queryImageInfos(pageUtils);
        return imageInfos;
    }

    @Override
    public Integer deleteImageInfoById(Integer id) {
        return imageInfoMapper.deleteImageById(id);
    }

    @Override
    public List<ImageInfoEntity> getImageInfosByIdList(List<Integer> idList) {
        return imageInfoMapper.queryImageInfosByIdList(idList);
    }
}
