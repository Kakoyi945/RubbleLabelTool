package com.label.rubblelabeltool.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.label.rubblelabeltool.entity.PointsEntity;
import com.label.rubblelabeltool.mapper.PointsMapper;
import com.label.rubblelabeltool.service.IPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPointsServiceImpl implements IPointsService {
    @Autowired(required = false)
    private PointsMapper pointsMapper;

    @Override
    public Integer storePoints(PointsEntity points) {
        return pointsMapper.insert(points);
    }

    @Override
    public PointsEntity getPointsById(Integer pid) {
        PointsEntity points = pointsMapper.queryPointsById(pid);
        // 补充points中的pointList
        String pointsStr = points.getPointsStr();
        JSONArray array = JSON.parseArray(pointsStr);
        List<Double[]> pointList = array.toJavaList(Double[].class);
        points.setPointList(pointList);
        return points;
    }

    @Override
    public List<Integer> getPointsIdsByImgId(Integer imgId) {
        return pointsMapper.queryPointsIdsByImgId(imgId);
    }

    @Override
    public List<PointsEntity> getPointsListByImgId(Integer imgId) {
        return pointsMapper.queryPointsListByImgId(imgId);
    }

    @Override
    public Integer deletePointsById(Integer id) {
        return pointsMapper.deletePointsById(id);
    }
}
