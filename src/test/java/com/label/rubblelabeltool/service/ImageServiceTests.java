package com.label.rubblelabeltool.service;

import com.label.rubblelabeltool.entity.ImageEntity;
import com.label.rubblelabeltool.entity.ImageInfoEntity;
import com.label.rubblelabeltool.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ImageServiceTests {
    @Autowired(required = false)
    IImageService iImageService;

    @Test
    public void storeImageInfoTest() {
        Integer cols = iImageService.storeImageInfo(7, "abc", "abc", "abc", new Date());
        System.out.println(cols);
    }
}
