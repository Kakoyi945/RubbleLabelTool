package com.label.rubblelabeltool.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageConfigurerTest {
    @Value("${web.upload-path}")
    private String path;

    @Test
    public void testBasePath() {

        System.out.println(path);
    }

}
