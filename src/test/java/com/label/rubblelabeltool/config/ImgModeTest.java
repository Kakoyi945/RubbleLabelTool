package com.label.rubblelabeltool.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImgModeTest {
    @Test
    public void getImgMode() {
        ImgMode rawIce = ImgMode.valueOf("RAWICE");
        System.out.println(rawIce == ImgMode.RAWICE);
    }
}
