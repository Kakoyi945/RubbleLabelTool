package com.label.rubblelabeltool.controller.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel("可以下载的图片（base64编码）")
public class DownloadBody {
    /**
     * 图片id
     */
    @ApiModelProperty(name = "img_id", value = "图片id")
    @JsonProperty("img_id")
    Integer imageId;
    /**
     * 图片（64编码）
     */
    @ApiModelProperty(name = "image", value = "图片（64编码）")
    @JsonProperty("image")
    String imgBase64;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadBody)) return false;
        DownloadBody that = (DownloadBody) o;
        return Objects.equals(imageId, that.imageId) && Objects.equals(imgBase64, that.imgBase64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, imgBase64);
    }

    @Override
    public String toString() {
        return "DownloadBody{" +
                "imageId=" + imageId +
                ", imgBase64='" + imgBase64 + '\'' +
                '}';
    }
}
