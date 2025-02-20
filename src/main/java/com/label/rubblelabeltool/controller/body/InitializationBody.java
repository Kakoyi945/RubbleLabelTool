package com.label.rubblelabeltool.controller.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.label.rubblelabeltool.entity.PointsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "InitializationBody", description = "点击编辑按钮时返回的数据，包括点集、点集id以及四种图像（如果都有的话）")
public class InitializationBody implements Serializable {

    /**
     * 点集数据
     */
    @ApiModelProperty(value = "点集的列表", dataType = "List")
    private List<PointsDataBody> pointsData;

    /**
     * 初始rgb图片
     */
    @ApiModelProperty(value = "初始rgb图片", dataType = "String")
    private String rawRgbImg;

    /**
     * ice图片
     */
    @ApiModelProperty(value = "初始ice图片", dataType = "String")
    private String iceImg;

    /**
     * 黑白图
     */
    @ApiModelProperty(value = "黑白图", dataType = "String")
    private String biImg;

    /**
     * 高亮图
     */
    @ApiModelProperty(value = "高亮图", dataType = "String")
    private String highLightImg;


    public List<PointsDataBody> getPointsData() {
        return pointsData;
    }

    public void setPointsData(List<PointsDataBody> pointsData) {
        this.pointsData = pointsData;
    }

    public String getRawRgbImg() {
        return rawRgbImg;
    }

    public void setRawRgbImg(String rawRgbImg) {
        this.rawRgbImg = rawRgbImg;
    }

    public String getIceImg() {
        return iceImg;
    }

    public void setIceImg(String iceImg) {
        this.iceImg = iceImg;
    }

    public String getBiImg() {
        return biImg;
    }

    public void setBiImg(String biImg) {
        this.biImg = biImg;
    }

    public String getHighLightImg() {
        return highLightImg;
    }

    public void setHighLightImg(String highLightImg) {
        this.highLightImg = highLightImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InitializationBody)) return false;
        InitializationBody that = (InitializationBody) o;
        return Objects.equals(pointsData, that.pointsData) && Objects.equals(rawRgbImg, that.rawRgbImg) && Objects.equals(iceImg, that.iceImg) && Objects.equals(biImg, that.biImg) && Objects.equals(highLightImg, that.highLightImg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointsData, rawRgbImg, iceImg, biImg, highLightImg);
    }

    @Override
    public String toString() {
        return "InitializationBody{" +
                "pointsData=" + pointsData +
                ", rawRgbImg='" + rawRgbImg + '\'' +
                ", iceImg='" + iceImg + '\'' +
                ", biImg='" + biImg + '\'' +
                ", highLightImg='" + highLightImg + '\'' +
                '}';
    }

}
