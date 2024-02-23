package com.label.rubblelabeltool.mapper;

import com.label.rubblelabeltool.entity.PointsEntity;

import java.util.List;

public interface PointsMapper {
    /**
     * 根据点集id获取点集对象，得到的对象中没有List属性，需要在service层中转化并添加
     * @param id
     * @return
     */
    PointsEntity queryPointsById(Integer id);

    /**
     * 插入点集，返回受到影响的行数
     * @param pointsEntity 点集对象
     * @return
     */
    Integer insert(PointsEntity pointsEntity);

    /**
     * 根据图片id查找所有的点集id
     * @param imgId 图片id
     * @return
     */
    List<Integer> queryPointsIdsByImgId(Integer imgId);

    /**
     * 通过图片id找到该图片上所有点集
     * @param imageId
     * @return
     */
    List<PointsEntity> queryPointsListByImgId(Integer imageId);

    /**
     * 通过点集id删除点集
     * @param id
     * @return
     */
    Integer deletePointsById(Integer id);

    /**
     * 通过图片id删除点集
     * @param imgId
     * @return
     */
    Integer deletePointsByImgId(Integer imgId);
}
