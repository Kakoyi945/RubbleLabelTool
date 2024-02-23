package com.label.rubblelabeltool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.Date;
import java.util.Objects;

public class ImageEntity extends BaseEntity{
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 图片宽度
     */
    private Double width;

    /**
     * 图片长度
     */
    private Double height;

    /**
     * rgb图片格式（jpeg或者png）
     */
    private String rgbImgType;

    /**
     * ice图片格式（jpeg或者png）
     */
    private String iceImgType;

    /**
     * 原始rgb图
     */
    private Mat rawRgbImg;

    /**
     * 原始ice图
     */
    private Mat rawIceImg;

    /**
     * 二值图
     */
    private Mat biImg;

    /**
     * 冰雪覆盖图
     */
    private Mat iceImg;

    /**
     * 高亮图
     */
    private Mat highLightImg;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Mat getRawRgbImg() {
        return rawRgbImg;
    }

    public void setRawRgbImg(Mat rawRgbImg) {
        this.rawRgbImg = rawRgbImg;
    }

    public Mat getRawIceImg() {
        return rawIceImg;
    }

    public void setRawIceImg(Mat rawIceImg) {
        this.rawIceImg = rawIceImg;
    }

    public Mat getBiImg() {
        return biImg;
    }

    public void setBiImg(Mat biImg) {
        this.biImg = biImg;
    }

    public Mat getIceImg() {
        return iceImg;
    }

    public void setIceImg(Mat iceImg) {
        this.iceImg = iceImg;
    }

    public Mat getHighLightImg() {
        return highLightImg;
    }

    public void setHighLightImg(Mat highLightImg) {
        this.highLightImg = highLightImg;
    }

    public String getRgbImgType() {
        return rgbImgType;
    }

    public void setRgbImgType(String rgbImgType) {
        this.rgbImgType = rgbImgType;
    }

    public String getIceImgType() {
        return iceImgType;
    }

    public void setIceImgType(String iceImgType) {
        this.iceImgType = iceImgType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageEntity)) return false;
        if (!super.equals(o)) return false;
        ImageEntity image = (ImageEntity) o;
        return Objects.equals(getId(), image.getId()) && Objects.equals(width, image.width) && Objects.equals(height, image.height) && Objects.equals(rawRgbImg, image.rawRgbImg) && Objects.equals(rawIceImg, image.rawIceImg) && Objects.equals(biImg, image.biImg) && Objects.equals(iceImg, image.iceImg) && Objects.equals(highLightImg, image.highLightImg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), width, height, rawRgbImg, rawIceImg, biImg, iceImg, highLightImg);
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "pid=" + getId() +
                ", width=" + width +
                ", height=" + height +
                ", rawRgbImg=" + rawRgbImg +
                ", rawIceImg=" + rawIceImg +
                ", biImg=" + biImg +
                ", iceImg=" + iceImg +
                ", highLightImg=" + highLightImg +
                '}';
    }
}
