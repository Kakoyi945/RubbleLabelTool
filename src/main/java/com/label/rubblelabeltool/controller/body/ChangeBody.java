package com.label.rubblelabeltool.controller.body;

import com.label.rubblelabeltool.entity.PointsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "changeBody", description = "标注图像后的返回体，包含原始点集信息以及图片（以base64编码）")
public class ChangeBody implements Serializable {
//    /**
//     * 点集信息
//     */
//    @ApiModelProperty(value = "点集信息", dataType = "List")
//    private List<PointsEntity> pointsEntityList;
    /**
     * 点集id的集合
     */
    @ApiModelProperty(value = "点集的id列表", dataType = "List")
    private List<Integer> pids;

    /**
     * 标注后的黑白图
     */
    @ApiModelProperty(value = "标注后的黑白图", dataType = "String")
    private String biImg;
    /**
     * 标注后的冰雪覆盖图
     */
    @ApiModelProperty(value = "标注后的冰雪覆盖图", dataType = "String")
    private String iceImg;
//    /**
//     * 标注后的高亮图
//     */
//    @ApiModelProperty(value = "标注后的冰雪覆盖图", dataType = "String")
//    private String highLightImg;


    public List<Integer> getPids() {
        return pids;
    }

    public void setPids(List<Integer> pids) {
        this.pids = pids;
    }

    public String getBiImg() {
        return biImg;
    }

    public void setBiImg(String biImg) {
        this.biImg = biImg;
    }

    public String getIceImg() {
        return iceImg;
    }

    public void setIceImg(String iceImg) {
        this.iceImg = iceImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeBody)) return false;
        ChangeBody that = (ChangeBody) o;
        return Objects.equals(pids, that.pids) && Objects.equals(biImg, that.biImg) && Objects.equals(iceImg, that.iceImg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pids, biImg, iceImg);
    }

    @Override
    public String toString() {
        return "ChangeBody{" +
                "pids=" + pids +
                ", biImg='" + biImg + '\'' +
                ", iceImg='" + iceImg + '\'' +
                '}';
    }
}
