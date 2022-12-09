package com.label.rubblelabeltool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "PointsEntity", description = "点集合，包括每个id，点集的行和列的列表")
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
    @JsonIgnore
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
        PointsEntity that = (PointsEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(imgId, that.imgId) && Objects.equals(pointList, that.pointList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), imgId, pointList);
    }

    @Override
    public String toString() {
        return "PointsEntity{" +
                "id=" + getId() +
                ", imgId=" + imgId +
                ", points=" + pointsStr +
                '}';
    }

    public List<Double> getRowPoints() {
        List<Double> rowPoints = new ArrayList<>();
        for(Double[] point: pointList) {
            rowPoints.add(point[1]);
        }
        return rowPoints;
    }

    public List<Double> getColPoints() {
        List<Double> colPoints = new ArrayList<>();
        for(Double[] point: pointList) {
            colPoints.add(point[0]);
        }
        return colPoints;
    }
    //    /**
//     * 点集id
//     */
//    @ApiModelProperty(value = "点集id", dataType = "int")
//    private Integer pid;
//    /**
//     * 点集的行坐标
//     */
//    @ApiModelProperty(value = "点集的行列表", dataType = "List")
//    private List<Double> rowPoints;
//    /**
//     * 点集的列坐标
//     */
//    @ApiModelProperty(value = "点集的列列表", dataType = "List")
//    private List<Double> colPoints;
//
//    public Integer getPid() {
//        return pid;
//    }
//
//    public void setPid(Integer pid) {
//        this.pid = pid;
//    }
//
//    public List<Double> getRowPoints() {
//        return rowPoints;
//    }
//
//    public void setRowPoints(List<Double> rowPoints) {
//        this.rowPoints = rowPoints;
//    }
//
//    public List<Double> getColPoints() {
//        return colPoints;
//    }
//
//    public void setColPoints(List<Double> colPoints) {
//        this.colPoints = colPoints;
//    }
//
//    @Override
//    public String getFileName() {
//        return super.getFileName();
//    }
//
//    @Override
//    public void setFileName(String fileName) {
//        super.setFileName(fileName);
//    }
//
//    @Override
//    public Integer getId() {
//        return super.getId();
//    }
//
//    @Override
//    public void setId(Integer id) {
//        super.setId(id);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        PointsEntity that = (PointsEntity) o;
//        return Objects.equals(pid, that.pid) && Objects.equals(rowPoints, that.rowPoints) && Objects.equals(colPoints, that.colPoints);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), pid, rowPoints, colPoints);
//    }
//
//    @Override
//    public String toString() {
//        return "PointsEntity{" +
//                "pid=" + pid +
//                ", rowPoints=" + rowPoints +
//                ", colPoints=" + colPoints +
//                '}';
//    }
}
