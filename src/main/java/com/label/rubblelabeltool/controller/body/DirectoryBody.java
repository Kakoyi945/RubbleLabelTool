package com.label.rubblelabeltool.controller.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class DirectoryBody implements Serializable {
    /**
     * 文件夹id
     */
    @JsonProperty("dir_id")
    Integer dirId;
    /**
     * 文件夹名字
     */
    @JsonProperty("dir_name")
    String dirName;
    /**
     * 文件夹大小
     */
    @JsonProperty("dir_size")
    Double dirSize;
    /**
     * 最后修改日期
     */
    @JsonProperty("edit_time")
    String lastEditDate;

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public Double getDirSize() {
        return dirSize;
    }

    public void setDirSize(Double dirSize) {
        this.dirSize = dirSize;
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryBody)) return false;
        DirectoryBody that = (DirectoryBody) o;
        return Objects.equals(dirId, that.dirId) && Objects.equals(dirName, that.dirName) && Objects.equals(dirSize, that.dirSize) && Objects.equals(lastEditDate, that.lastEditDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dirId, dirName, dirSize, lastEditDate);
    }

    @Override
    public String toString() {
        return "DirectoryBody{" +
                "dirId=" + dirId +
                ", dirName='" + dirName + '\'' +
                ", dirSize=" + dirSize +
                ", lastEditDate=" + lastEditDate +
                '}';
    }
}
