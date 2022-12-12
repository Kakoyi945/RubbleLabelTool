package com.label.rubblelabeltool.controller.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.label.rubblelabeltool.entity.PointsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "PointsEntriesBody", description = "点集以及图片id对象")
public class PointsEntriesBody implements Serializable {
    /**
     * 点集的集合
     */
    @ApiModelProperty("点集集合对象")
    @JsonProperty("ptses")
    List<List<Double[]>> points;
    /**
     * 图片的id
     */
    @ApiModelProperty("图片id")
    @JsonProperty("image_id")
    Integer imageId;

    public List<List<Double[]>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Double[]>> points) {
        this.points = points;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointsEntriesBody)) return false;
        PointsEntriesBody pointsEntriesBody = (PointsEntriesBody) o;
        return Objects.equals(points, pointsEntriesBody.points) && Objects.equals(imageId, pointsEntriesBody.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points, imageId);
    }

    @Override
    public String toString() {
        return "PtesBody{" +
                "points=" + points.toString() +
                ", imageId=" + imageId.toString() +
                '}';
    }
}
