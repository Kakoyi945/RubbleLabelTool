package com.label.rubblelabeltool.controller.body;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PageResultBody implements Serializable {
    /**
     * 总记录数
     */
    private Integer totalCount;
    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 当前页数
     */
    private Integer curPage;
    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 分页结果
     * @param list
     * @param totalCount
     * @param pageSize
     * @param curPage
     */
    public PageResultBody(List<?> list, Integer totalCount, Integer pageSize, Integer curPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.curPage = curPage;
        this.totalPage = (int) Math.ceil((double) totalCount/pageSize);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public List<?> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageResultBody)) return false;
        PageResultBody that = (PageResultBody) o;
        return Objects.equals(totalCount, that.totalCount) && Objects.equals(pageSize, that.pageSize) && Objects.equals(totalPage, that.totalPage) && Objects.equals(curPage, that.curPage) && Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCount, pageSize, totalPage, curPage, list);
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResultBody{" +
                "totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", curPage=" + curPage +
                ", list=" + list +
                '}';
    }
}
