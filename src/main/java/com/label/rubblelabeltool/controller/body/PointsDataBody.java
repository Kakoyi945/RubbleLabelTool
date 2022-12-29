package com.label.rubblelabeltool.controller.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PointsDataBody implements Serializable {
    /**
     * 点集id
     */
    @JsonProperty("pids")
    Integer pid;
    /**
     * 点集
     */
    @JsonProperty("points")
    List<Double[]> points;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public List<Double[]> getPoints() {
        return points;
    }

    public void setPoints(List<Double[]> points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointsDataBody)) return false;
        PointsDataBody that = (PointsDataBody) o;
        return Objects.equals(pid, that.pid) && Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, points);
    }

    @Override
    public String toString() {
        return "PointsDataBody{" +
                "pid=" + pid +
                ", points=" + points +
                '}';
    }
}
