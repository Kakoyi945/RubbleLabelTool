package com.label.rubblelabeltool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import java.util.Date;
import java.util.Objects;

@ApiModel("图片信息")
public class ImageInfoEntity extends BaseEntity{
    /**
     * 原始rgb文件路径
     */
    @JsonIgnore
    private String rawRgbPath;
    /**
     * 原始冰雪覆盖图路径
     */
    @JsonIgnore
    private String rawIcePath;
    /**
     * rgb路径
     */
    @JsonIgnore
    private String rgbPath;
    /**
     * 黑白图路径
     */
    @JsonIgnore
    private String biPath;
    /**
     * 冰雪覆盖图路径
     */
    @JsonIgnore
    private String icePath;
    /**
     * 高亮图路径
     */
    @JsonIgnore
    private String highLightPath;
    /**
     * 是否标注完成，默认为false
     */
    @JsonIgnore
    private boolean isLabeled = false;
    /**
     * 上次编辑时间
     */
    private Date editTime;
    /**
     * 图片大小
     */
    private Integer size;
    /**
     * 图片类型
     */
    private String type;
    /**
     * 图片是否被删除
     */
    @JsonIgnore
    private Boolean isDeleted = false;

    public String getRawRgbPath() {
        return rawRgbPath;
    }

    public void setRawRgbPath(String rawRgbPath) {
        this.rawRgbPath = rawRgbPath;
    }

    public String getRawIcePath() {
        return rawIcePath;
    }

    public void setRawIcePath(String rawIcePath) {
        this.rawIcePath = rawIcePath;
    }

    public String getRgbPath() {
        return rgbPath;
    }

    public void setRgbPath(String rgbPath) {
        this.rgbPath = rgbPath;
    }

    public String getBiPath() {
        return biPath;
    }

    public void setBiPath(String biPath) {
        this.biPath = biPath;
    }

    public String getIcePath() {
        return icePath;
    }

    public void setIcePath(String icePath) {
        this.icePath = icePath;
    }

    public String getHighLightPath() {
        return highLightPath;
    }

    public void setHighLightPath(String highLightPath) {
        this.highLightPath = highLightPath;
    }

    public boolean isLabeled() {
        return isLabeled;
    }

    public void setLabeled(boolean labeled) {
        isLabeled = labeled;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageInfoEntity)) return false;
        if (!super.equals(o)) return false;
        ImageInfoEntity imageInfo = (ImageInfoEntity) o;
        return Objects.equals(getId(), imageInfo.getId()) && Objects.equals(getFileName(), imageInfo.getFileName()) && isLabeled == imageInfo.isLabeled && Objects.equals(rawRgbPath, imageInfo.rawRgbPath) && Objects.equals(rawIcePath, imageInfo.rawIcePath) && Objects.equals(rgbPath, imageInfo.rgbPath) && Objects.equals(biPath, imageInfo.biPath) && Objects.equals(icePath, imageInfo.icePath) && Objects.equals(highLightPath, imageInfo.highLightPath) && Objects.equals(editTime, imageInfo.editTime) && Objects.equals(size, imageInfo.size) && Objects.equals(type, imageInfo.type) && Objects.equals(isDeleted, imageInfo.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rawRgbPath, rawIcePath, rgbPath, biPath, icePath, highLightPath, isLabeled, editTime, size, type, isDeleted);
    }

    @Override
    public String toString() {
        return "ImageInfoEntity{" +
                "id='" + getId() + '\'' +
                ", fileName='" + getFileName() + '\'' +
                ", rawRgbPath='" + rawRgbPath + '\'' +
                ", rawIcePath='" + rawIcePath + '\'' +
                ", rgbPath='" + rgbPath + '\'' +
                ", biPath='" + biPath + '\'' +
                ", icePath='" + icePath + '\'' +
                ", highLightPath='" + highLightPath + '\'' +
                ", isLabeled=" + isLabeled +
                ", editTime=" + editTime +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
