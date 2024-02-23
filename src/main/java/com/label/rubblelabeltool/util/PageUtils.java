package com.label.rubblelabeltool.util;

import java.util.LinkedHashMap;
import java.util.Map;


public class PageUtils extends LinkedHashMap<String, Object> {
    private Integer page;
    private Integer limit;
    private Integer imgMode;

    public PageUtils(Map<String, Object> params, Integer isPaged) {
        this.putAll(params);

        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.imgMode = Integer.parseInt(params.get("imgMode").toString());

        if(isPaged == 0) {
            this.put("start", null);
            this.put("page", null);
            this.put("limit", null);
            this.put("imgMode", null);
        } else {
            this.put("start", (page-1) * limit);
            this.put("page", page);
            this.put("limit", limit);
            this.put("imgMode", imgMode);
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getImgMode() {
        return imgMode;
    }

    public void setImgMode(Integer imgMode) {
        this.imgMode = imgMode;
    }
}
