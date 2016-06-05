package com.abs.commons.search;

/**
 * @author hao.wang
 * @since 2016/4/28 18:10
 */
public class PageRequest {

    public final static int DEFAULT_PAGE_SIZE = 20;

    private final int page;

    private final int pageSize;

    public PageRequest(int page, int pageSize) {
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.page = page;
        this.pageSize = pageSize;
    }

    public PageRequest(int page) {
        this(page, DEFAULT_PAGE_SIZE);
    }

    public PageRequest() {
        this(1, DEFAULT_PAGE_SIZE);
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getOffset() {
        return pageSize * (page - 1);
    }

}
