package com.label.rubblelabeltool.service;

import com.label.rubblelabeltool.config.ImgMode;
import com.label.rubblelabeltool.controller.body.PageResultBody;
import com.label.rubblelabeltool.entity.ImageEntity;
import com.label.rubblelabeltool.entity.ImageInfoEntity;
import com.label.rubblelabeltool.entity.PointsEntity;
import com.label.rubblelabeltool.util.PageUtil;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 图像服务接口
 */
public interface IImageService {
//    /**
//     * 上传图片
//     * @param imageEntity
//     */
//    void uploadImage(ImageEntity imageEntity);

    /**
     * 补充imageInfo，主要是图片路径和上传日期，并写入数据库
     * @param imageName 图片名字
     * @param uploadTime 上传日期
     * @param imgModeInt 图片模式(int)
     * @param path 文件保存路径
     * @param size 文件大小
     * @param type 文件类型
     * @return 受影响的行数
     */
    Integer uploadImage(String imageName, Date uploadTime, Integer imgModeInt, String path, Double size, String type);

    /**
     * 补充imageInfo，主要是图片路径
     * @param imageInfo 图片信息
     * @param path 图片路径
     * @param imgMode 图片模式
     * @return
     */
    Integer updateImage(ImageInfoEntity imageInfo, String path, Integer imgMode);

    /**
     * 根据图片id找到图片
     * @param id 图片id
     * @return 图片信息
     */
    ImageInfoEntity getImageInfoById(Integer id);

    /**
     * 根据所给的点集进行标注，并返回标注后的图像
     * @param image 3种图片矩阵以及图片宽高
     * @param pts 点集
     * @return ImageEntity 返回所有图片
     */
    ImageEntity changeImage(ImageEntity image, List<PointsEntity> pts);

    /**
     * 根据图片id将图片信息（主要是标注图片的位置）保存到数据库中
     * @param id 图片id
     * @param biPath 黑白图路径
     * @param icePath 冰雪覆盖图路径
     * @param highLightPath 高亮图路径
     * @param labeledTime 标注完成时间
     * @return
     */
    Integer storeImageInfo(Integer id, String biPath, String icePath, String highLightPath, Date labeledTime);

    /**
     * 根据图片名字得到图片信息
     * @param fileName
     * @return
     */
    ImageInfoEntity getImageInfoByFileName(String fileName);

    /**
     * 实现分页工农
     * @param pageUtil
     * @return
     */
    List<ImageInfoEntity> getImageInfos(PageUtil pageUtil);

    /**
     * 根据图片id删除图片
     * @param id
     * @return
     */
    Integer deleteImageInfoById(Integer id);

    /**
     * 根据id列表获取图片信息
     * @param idList
     * @return
     */
    List<ImageInfoEntity> getImageInfosByIdList(List<Integer> idList);
}
