package com.label.rubblelabeltool.service;

import com.label.rubblelabeltool.entity.PointsEntity;

import java.util.List;

public interface IPointsService {
    /**
     * 将点集插入数据库中
     * @param points
     * @return
     */
    Integer storePoints(PointsEntity points);

    /**
     * 根据pid找到对应的点集，由于查到的点集中无List属性，需要在实现类中进行JSON转换
     * @param pid
     * @return
     */
    PointsEntity getPointsById(Integer pid);

    /**
     * 根据图片id找到所有点集id
     * @param imgId
     * @return
     */
    List<Integer> getPointsIdsByImgId(Integer imgId);

    /**
     * 根据图片id返回所有点集
     * @param imgId
     * @return
     */
    List<PointsEntity> getPointsListByImgId(Integer imgId);

    /**
     * 根据id删除点集
     * @param id
     * @return
     */
    Integer deletePointsById(Integer id);
}
