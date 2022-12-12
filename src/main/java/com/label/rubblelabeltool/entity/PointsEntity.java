package com.label.rubblelabeltool.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PointsEntity extends BaseEntity{
    /**
     * 图片id
     */
    private Integer imgId;
    /**
     * 点集
     */
    private List<Double[]> pointList = new ArrayList<>();
    /**
     * 点集的String形式，用于保存到数据库中
     */
    private String pointsStr;

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public List<Double[]> getPointList() {
        return pointList;
    }

    public void setPointList(List<Double[]> pointList) {
        this.pointList = pointList;
    }

    public String getPointsStr() {
        return pointsStr;
    }

    public void setPointsStr(String pointsStr) {
        this.pointsStr = pointsStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointsEntity)) return false;
        if (!super.equals(o)) return false;
        PointsEntity points = (PointsEntity) o;
        return Objects.equals(imgId, points.imgId) && Objects.equals(pointList, points.pointList) && Objects.equals(pointsStr, points.pointsStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), imgId, pointList, pointsStr);
    }

    @Override
    public String toString() {
        return "PointsEntity{" +
                "imgId=" + imgId +
                ", pointList=" + pointList +
                ", pointsStr='" + pointsStr + '\'' +
                '}';
    }

    @JsonIgnore
    public List<Double> getRowPoints(){
        List<Double> rowPoints = new ArrayList<>();
        for(Double[] point: pointList){
            rowPoints.add(point[1]);
        }
        return rowPoints;
    }

    @JsonIgnore
    public List<Double> getColPoints(){
        List<Double> colPoints = new ArrayList<>();
        for(Double[] point: pointList){
            colPoints.add(point[0]);
        }
        return colPoints;
    }
}
