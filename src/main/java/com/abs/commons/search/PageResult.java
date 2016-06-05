package com.abs.commons.search;

import java.util.List;

/**
 * @author hao.wang
 * @since 2016/4/28 12:06
 */
public class PageResult<T> {

    private List<T> data;
    private PageInfo pageInfo;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
