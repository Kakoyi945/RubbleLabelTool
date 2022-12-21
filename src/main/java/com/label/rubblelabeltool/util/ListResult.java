package com.label.rubblelabeltool.util;

import java.io.Serializable;
import java.util.List;

public class ListResult<E> implements Serializable {
    /**
     * 状态码
     */
    private Integer state;
    /**
     * 描述信息
     */
    private String message;
    /**
     * 数据
     */
    private List<E> list;

    public ListResult(Integer state, List<E> list) {
        this.state = state;
        this.list = list;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}
