package com.github.pagehelper;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.RowBounds;

/* loaded from: pagehelper-3.7.4.jar:com/github/pagehelper/Page.class */
public class Page<E> extends ArrayList<E> {
    private static final long serialVersionUID = 1;
    private static final int NO_SQL_COUNT = -1;
    private static final int SQL_COUNT = 0;
    private int pageNum;
    private int pageSize;
    private int startRow;
    private int endRow;
    private long total;
    private int pages;
    private Boolean reasonable;
    private Boolean pageSizeZero;

    public Page() {
    }

    public Page(int pageNum, int pageSize) {
        this(pageNum, pageSize, 0, null);
    }

    public Page(int pageNum, int pageSize, boolean count) {
        this(pageNum, pageSize, count ? 0 : -1, null);
    }

    private Page(int pageNum, int pageSize, int total, Boolean reasonable) {
        super(0);
        if (pageNum == 1 && pageSize == Integer.MAX_VALUE) {
            this.pageSizeZero = true;
            pageSize = 0;
        }
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        calculateStartAndEndRow();
        setReasonable(reasonable);
    }

    public Page(RowBounds rowBounds, boolean count) {
        this(rowBounds, count ? 0 : -1);
    }

    public Page(RowBounds rowBounds, int total) {
        super(0);
        if (rowBounds.getOffset() == 0 && rowBounds.getLimit() == Integer.MAX_VALUE) {
            this.pageSizeZero = true;
            this.pageSize = 0;
        } else {
            this.pageSize = rowBounds.getLimit();
        }
        this.startRow = rowBounds.getOffset();
        this.total = total;
        this.endRow = this.startRow + rowBounds.getLimit();
    }

    public List<E> getResult() {
        return this;
    }

    public int getPages() {
        return this.pages;
    }

    public int getEndRow() {
        return this.endRow;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = (this.reasonable == null || !this.reasonable.booleanValue() || pageNum > 0) ? pageNum : 1;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return this.startRow;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
        if (this.pageSize > 0) {
            this.pages = (int) ((total / this.pageSize) + (total % ((long) this.pageSize) == 0 ? 0 : 1));
        } else {
            this.pages = 0;
        }
        if (this.reasonable != null && this.reasonable.booleanValue() && this.pageNum > this.pages) {
            this.pageNum = this.pages;
            calculateStartAndEndRow();
        }
    }

    public void setReasonable(Boolean reasonable) {
        if (reasonable == null) {
            return;
        }
        this.reasonable = reasonable;
        if (this.reasonable.booleanValue() && this.pageNum <= 0) {
            this.pageNum = 1;
            calculateStartAndEndRow();
        }
    }

    public Boolean getReasonable() {
        return this.reasonable;
    }

    public Boolean getPageSizeZero() {
        return this.pageSizeZero;
    }

    public void setPageSizeZero(Boolean pageSizeZero) {
        if (pageSizeZero != null) {
            this.pageSizeZero = pageSizeZero;
        }
    }

    private void calculateStartAndEndRow() {
        this.startRow = this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
        this.endRow = this.startRow + (this.pageSize * (this.pageNum > 0 ? 1 : 0));
    }

    public boolean isCount() {
        return this.total > -1;
    }

    public Page<E> pageNum(int pageNum) {
        this.pageNum = (this.reasonable == null || !this.reasonable.booleanValue() || pageNum > 0) ? pageNum : 1;
        return this;
    }

    public Page<E> pageSize(int pageSize) {
        this.pageSize = pageSize;
        calculateStartAndEndRow();
        return this;
    }

    public Page<E> count(Boolean count) {
        this.total = count.booleanValue() ? 0L : -1L;
        return this;
    }

    public Page<E> reasonable(Boolean reasonable) {
        setReasonable(reasonable);
        return this;
    }

    public Page<E> pageSizeZero(Boolean pageSizeZero) {
        setPageSizeZero(pageSizeZero);
        return this;
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuffer sb = new StringBuffer("Page{");
        sb.append("pageNum=").append(this.pageNum);
        sb.append(", pageSize=").append(this.pageSize);
        sb.append(", startRow=").append(this.startRow);
        sb.append(", endRow=").append(this.endRow);
        sb.append(", total=").append(this.total);
        sb.append(", pages=").append(this.pages);
        sb.append(", reasonable=").append(this.reasonable);
        sb.append(", pageSizeZero=").append(this.pageSizeZero);
        sb.append('}');
        return sb.toString();
    }
}
