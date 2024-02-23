package com.label.rubblelabeltool.mapper;

import com.label.rubblelabeltool.entity.ImageEntity;
import com.label.rubblelabeltool.entity.ImageInfoEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ImageInfoMapper {

    /**
     * 插入图片信息
     * @param imageInfo 图片信息
     * @return 受影响的行数
     */
    Integer insert(ImageInfoEntity imageInfo);

    /**
     * 根据id查找图片信息
     * @param id 图片id
     * @return 图片信息
     */
    ImageInfoEntity queryById(Integer id);

    /**
     * 根据id更新标注完成日期
     * @param id 图片id
     * @param labeledTime 标注完成时间
     * @return
     */
    Integer updateLabeledTimeById(Integer id, Date labeledTime);

    /**
     * 根据id更新标注完图片的路径以及日期，并将是否标注属性改为已标注
     * @param id 图片id
     * @param biPath 二值图路径
     * @param icePath 冰雪覆盖图路径
     * @param highLightPath 高亮图路径
     * @param labeledTime 标注完成时间
     * @return 受影响的行数
     */
    Integer updateLabeledImagesInfoById(Integer id, String biPath, String icePath, String highLightPath, Date labeledTime);

    /**
     * 根据文件名得到id
     * @param fileName 文件名
     * @return id
     */
    List<Integer> queryImageIdByImageName(String fileName);

    /**
     * 根据图片名获取图片
     * @param fileName
     * @return
     */
    ImageInfoEntity queryImagInfoByImageName(String fileName);

    /**
     * 根据id更新图片信息的raw rgb位置信息
     * @param id 图片id
     * @param rawRgbPath
     * @return
     */
    Integer updateRawRgbPathById(Integer id, String rawRgbPath);

    /**
     * 根据id更新图片信息的raw ice位置信息
     * @param id 图片id
     * @param rawIcePath
     * @return
     */
    Integer updateRawIcePathById(Integer id, String rawIcePath);

    /**
     * 根据start和limit获取固定数量的ImageInfo
     * @param param 分页需要的参数
     * @return
     */
    List<ImageInfoEntity> queryImageInfos(Map param);

    /**
     * 查询图片总数
     * @return
     */
    Integer queryTotalImages();

    /**
     * 根据图片id删除图片（将is_deleted置为1）
     * @param id
     * @return
     */
    Integer deleteImageById(Integer id);

    /**
     * 根据idList查询多张图片的信息
     * @param idList
     * @return
     */
    List<ImageInfoEntity> queryImageInfosByIdList(List<Integer> idList);

    /**
     * 获取没有删除的图片数量
     * @return
     */
    Integer queryTotalCountByImgMode(Integer imgMode);

    /**
     * 根据图片id将标注图片删去
     * @param id
     * @return
     */
    Integer dislabelImageById(Integer id);
}
