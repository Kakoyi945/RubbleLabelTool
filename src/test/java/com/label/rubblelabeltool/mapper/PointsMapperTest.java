package com.label.rubblelabeltool.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.label.rubblelabeltool.entity.PointsEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PointsMapperTest {
    @Autowired(required = false)
    private PointsMapper pointsMapper;

    @Test
    public void insertTest() {
        PointsEntity points = new PointsEntity();
        points.setImgId(42);
        List<double[]> pointList = new ArrayList<>();
        double[] point0 = {1.0, 2.0};
        double[] point1 = {3.0, 2.0};
        double[] point2 = {5.0, 7.0};
        double[] point3 = {2.0, 7.0};
        pointList.add(point0);
        pointList.add(point1);
        pointList.add(point2);
        pointList.add(point3);
        String s = JSONObject.toJSONString(pointList);
        points.setPointsStr(s);
        System.out.println(s);
        JSONArray array = JSON.parseArray(s);
        List<double[]> list = array.toJavaList(double[].class);
        for(double[] point: list) {
            System.out.println(point[0] + " " + point[1]);
        }
        Integer cols = pointsMapper.insert(points);
        System.out.println(cols);
    }

    @Test
    public void queryIdsByImgIdTest(){
        Integer imgId = 42;
        List<Integer> ids = pointsMapper.queryPointsIdsByImgId(imgId);
        System.out.println(ids);
    }

    @Test
    public void queryPointsByImgIdTest() {
        Integer imgId = 42;
        List<PointsEntity> pointsEntities = pointsMapper.queryPointsListByImgId(imgId);
        for(PointsEntity pointsEntity: pointsEntities){
            System.out.println(pointsEntity);
        }
    }

    @Test
    public void deleteTest(){
        Integer pid = 1;
        Integer cols = pointsMapper.deletePointsById(pid);
        System.out.println(cols);
    }
}
