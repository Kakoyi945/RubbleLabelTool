package com.label.rubblelabeltool.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 实体类的基类
 */
public class BaseEntity implements Serializable {
    /**
     * id
     */
    @JsonIgnore
    private Integer id;
    /**
     * 文件名
     */
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
