package com.label.rubblelabeltool.service;

import com.alibaba.fastjson.JSONObject;
import com.label.rubblelabeltool.entity.PointsEntity;
import com.label.rubblelabeltool.mapper.PointsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PointsServiceTest {
    @Autowired(required = false)
    private IPointsService iPointsService;

    @Test
    public void storePointsTest() {
        PointsEntity points = new PointsEntity();
        points.setImgId(42);
        List<Double[]> pointList = new ArrayList<>();
        Double[] point0 = {2.0, 2.0};
        Double[] point1 = {3.0, 2.0};
        Double[] point2 = {5.0, 7.5};
        Double[] point3 = {9.0, 7.0};
        pointList.add(point0);
        pointList.add(point1);
        pointList.add(point2);
        pointList.add(point3);
        points.setPointList(pointList);
        String s = JSONObject.toJSONString(pointList);
        points.setPointsStr(s);
        Integer cols = iPointsService.storePoints(points);
        System.out.println(cols);
    }

    @Test
    public void getPointsByIdTest() {
        Integer pid = 1;
        PointsEntity points = iPointsService.getPointsById(pid);
        System.out.println(points.toString());
    }
}
