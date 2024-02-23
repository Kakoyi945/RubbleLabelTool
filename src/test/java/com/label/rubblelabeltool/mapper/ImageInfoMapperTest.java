package com.label.rubblelabeltool.mapper;

import com.label.rubblelabeltool.entity.ImageInfoEntity;
import com.label.rubblelabeltool.util.PageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageInfoMapperTest {
    @Autowired(required = false)
    private ImageInfoMapper imageInfoMapper;

    @Test
    public void insert() {
        ImageInfoEntity imageInfo = new ImageInfoEntity();
        imageInfo.setFileName("test");
        Date date = new Date(new java.util.Date().getTime());
        imageInfo.setEditTime(date);
        System.out.println(imageInfo.toString());
        Integer cols = imageInfoMapper.insert(imageInfo);
        System.out.println(cols);
    }

    @Test
    public void queryById() {
        Integer id = 1;
        ImageInfoEntity imageInfo = imageInfoMapper.queryById(id);
        System.out.println(imageInfo.toString());
    }

    @Test
    public void updateLabeledTimeByIdTest() {
        Integer id = 1;
        Date date = new Date(new java.util.Date().getTime());
        Integer cols = imageInfoMapper.updateLabeledTimeById(id, date);
        System.out.println(cols);
    }

    @Test
    public void updateLabeledImagesInfoByIdTest() {
        Integer id = 8;
        String biPath = "abc";
        String icePath = "abc";
        String highLightPath = "abc";
        Date labeledTime = new Date(new java.util.Date().getTime());
        Integer cols = imageInfoMapper.updateLabeledImagesInfoById(id, biPath, icePath, highLightPath, labeledTime);
        System.out.println(cols);
    }

    @Test
    public void queryIdByImageNameTest() {
        String fileName = "image";
        List<Integer> id = imageInfoMapper.queryImageIdByImageName(fileName);
        System.out.println("-------------------------------------"+id);
    }

    @Test
    public void queryImageInfoByImageNameTest() {
        String fileName = "qqq";
        ImageInfoEntity imageInfo = imageInfoMapper.queryImagInfoByImageName(fileName);
        System.out.println(imageInfo);
    }

    @Test
    public void updateRawRgbPathByIdTest() {
        Integer id = 5;
        Integer cols = imageInfoMapper.updateRawRgbPathById(id, "abc");
        System.out.println(cols);
    }

    @Test
    public void updateRawIcePathByIdTest() {
        Integer id = 5;
        Integer cols = imageInfoMapper.updateRawIcePathById(id, "abc");
        System.out.println(cols);
    }

    @Test
    public void pageTest(){
        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("page", 1);
        pageInfoMap.put("limit", 4);
        pageInfoMap.put("imgMode", 0);
        PageUtils pageUtils = new PageUtils(pageInfoMap, 1);
        List<ImageInfoEntity> imageInfos = imageInfoMapper.queryImageInfos(pageUtils);
        for(ImageInfoEntity imageInfo: imageInfos){
            System.out.println(imageInfo.toString());
        }
    }

    @Test
    public void deleteTest() {
        Integer imgId = 41;
        Integer cols = imageInfoMapper.deleteImageById(imgId);
        System.out.println(cols);
    }

    @Test
    public void queryListTest() {
        List<Integer> list = new ArrayList<>();
        list.add(42); list.add(43);
        List<ImageInfoEntity> imageInfos = imageInfoMapper.queryImageInfosByIdList(list);
        for(ImageInfoEntity imageInfo : imageInfos) {
            System.out.println(imageInfo.toString());
        }
    }

    @Test
    public void queryTotalCountByImgModeTest() {
        Integer imgMode = 2;
        Integer totals = imageInfoMapper.queryTotalCountByImgMode(imgMode);
        System.out.println(totals);
    }
}

