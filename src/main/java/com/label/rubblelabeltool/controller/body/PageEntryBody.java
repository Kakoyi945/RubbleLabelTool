package com.label.rubblelabeltool.controller.body;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PageEntryBody implements Serializable {
    private String fileName;
    private String type;
    private Double size;
    private Date editTime;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageEntryBody)) return false;
        PageEntryBody that = (PageEntryBody) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(type, that.type) && Objects.equals(size, that.size) && Objects.equals(editTime, that.editTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, type, size, editTime);
    }

    @Override
    public String toString() {
        return "PageEntryBody{" +
                "fileName='" + fileName + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", editTime=" + editTime +
                '}';
    }
}
