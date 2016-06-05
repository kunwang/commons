package com.abs.commons.search;

/**
 * @author hao.wang
 * @since 2016/4/28 11:54
 */
public class PageInfo {
    private static final int PAGE_SIZE = 20;

    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 200;

    private int totalCount;
    private int totalPage;
    private int currentPage;
    private int pageSize;

    public PageInfo(int totalCount, int pageSize, int page) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = this.countTotalPage(this.pageSize, this.totalCount);
        this.setCurrentPage(page);
    }

    public PageInfo(int totalCount, int page) {
        this(totalCount, PAGE_SIZE, page);
    }

    public PageInfo(int totalCount) {
        this(totalCount, PAGE_SIZE, 1);
    }

    public int getTotalCount() {
        return this.totalCount;
    }


    public int getTotalPage() {
        return this.totalPage;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    private void setCurrentPage(int currentPage) {
        if (currentPage > this.totalPage) {
            this.currentPage = totalPage;
        } else if (currentPage < 1) {
            this.currentPage = 1;
        } else {
            this.currentPage = currentPage;
        }
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getBeginIndex() {
        int beginIndex = (currentPage - 1) * pageSize;
        return beginIndex;
    }

    public int countTotalPage(int pageSize, int totalCount) {
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize
                : totalCount / pageSize + 1;
        return totalPage;
    }

    public int getNextPage() {
        if (currentPage + 1 >= this.totalPage) {
            return this.totalPage;
        }
        return currentPage + 1;
    }

    public int getPreviousPage() {
        if (currentPage - 1 <= 1) {
            return 1;
        } else {
            return currentPage - 1;
        }
    }

    public boolean isHasPrevious() {
        return this.hasPrevious();
    }

    public boolean isHasNext() {
        return this.hasNext();
    }

    private boolean hasNext() {
        return currentPage < this.totalPage;
    }

    private boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean isFirst() {
        return currentPage == 1;
    }

    public boolean isLast() {
        return currentPage >= this.totalPage;
    }

}
