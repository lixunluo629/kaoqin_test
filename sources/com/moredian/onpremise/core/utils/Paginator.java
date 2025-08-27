package com.moredian.onpremise.core.utils;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ApiModel("分页器对象,请求时只需要传pageNum及pageSize")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/Paginator.class */
public class Paginator implements Serializable {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(name = "pageNum", value = "起始页数")
    private int pageNum;

    @ApiModelProperty(name = "pageSize", value = "每页查询数")
    private int pageSize;

    @ApiModelProperty(name = "startRow", value = "开始行", hidden = true)
    private int startRow;

    @ApiModelProperty(name = "endRow", value = "结束行", hidden = true)
    private int endRow;

    @ApiModelProperty(name = "total", value = "总数")
    private long total;

    @ApiModelProperty(hidden = true)
    private int pages;

    @ApiModelProperty(name = "firstPage", value = "第一页", hidden = true)
    private int firstPage;

    @ApiModelProperty(name = "prePage", value = "上一页", hidden = true)
    private int prePage;

    @ApiModelProperty(name = "nextPage", value = "下一页", hidden = true)
    private int nextPage;

    @ApiModelProperty(name = "lastPage", value = "最后一页", hidden = true)
    private int lastPage;

    @ApiModelProperty(name = "isFirstPage", value = "是否是第一页", hidden = true)
    private boolean isFirstPage;

    @ApiModelProperty(name = "isLastPage", value = "是否是最后一页", hidden = true)
    private boolean isLastPage;

    @ApiModelProperty(name = "hasPreviousPage", value = "是否有上一页", hidden = true)
    private boolean hasPreviousPage;

    @ApiModelProperty(name = "hasNextPage", value = "是否有下一页", hidden = true)
    private boolean hasNextPage;

    @ApiModelProperty(hidden = true)
    private int navigatePages;

    @ApiModelProperty(hidden = true)
    private int[] navigatepageNums;

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Paginator)) {
            return false;
        }
        Paginator other = (Paginator) o;
        return other.canEqual(this) && getPageNum() == other.getPageNum() && getPageSize() == other.getPageSize() && getStartRow() == other.getStartRow() && getEndRow() == other.getEndRow() && getTotal() == other.getTotal() && getPages() == other.getPages() && getFirstPage() == other.getFirstPage() && getPrePage() == other.getPrePage() && getNextPage() == other.getNextPage() && getLastPage() == other.getLastPage() && getFirstPage() == other.getFirstPage() && getLastPage() == other.getLastPage() && isHasPreviousPage() == other.isHasPreviousPage() && isHasNextPage() == other.isHasNextPage() && getNavigatePages() == other.getNavigatePages() && Arrays.equals(getNavigatepageNums(), other.getNavigatepageNums());
    }

    protected boolean canEqual(Object other) {
        return other instanceof Paginator;
    }

    public int hashCode() {
        int result = (1 * 59) + getPageNum();
        int result2 = (((((result * 59) + getPageSize()) * 59) + getStartRow()) * 59) + getEndRow();
        long $total = getTotal();
        return (((((((((((((((((((((((result2 * 59) + ((int) (($total >>> 32) ^ $total))) * 59) + getPages()) * 59) + getFirstPage()) * 59) + getPrePage()) * 59) + getNextPage()) * 59) + getLastPage()) * 59) + getFirstPage()) * 59) + getLastPage()) * 59) + (isHasPreviousPage() ? 79 : 97)) * 59) + (isHasNextPage() ? 79 : 97)) * 59) + getNavigatePages()) * 59) + Arrays.hashCode(getNavigatepageNums());
    }

    public String toString() {
        return "Paginator(pageNum=" + getPageNum() + ", pageSize=" + getPageSize() + ", startRow=" + getStartRow() + ", endRow=" + getEndRow() + ", total=" + getTotal() + ", pages=" + getPages() + ", firstPage=" + getFirstPage() + ", prePage=" + getPrePage() + ", nextPage=" + getNextPage() + ", lastPage=" + getLastPage() + ", isFirstPage=" + getFirstPage() + ", isLastPage=" + getLastPage() + ", hasPreviousPage=" + isHasPreviousPage() + ", hasNextPage=" + isHasNextPage() + ", navigatePages=" + getNavigatePages() + ", navigatepageNums=" + Arrays.toString(getNavigatepageNums()) + ")";
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getStartRow() {
        return this.startRow;
    }

    public int getEndRow() {
        return this.endRow;
    }

    public long getTotal() {
        return this.total;
    }

    public int getPages() {
        return this.pages;
    }

    public int getFirstPage() {
        return this.firstPage;
    }

    public int getPrePage() {
        return this.prePage;
    }

    public int getNextPage() {
        return this.nextPage;
    }

    public int getLastPage() {
        return this.lastPage;
    }

    public boolean isHasPreviousPage() {
        return this.hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return this.hasNextPage;
    }

    public int getNavigatePages() {
        return this.navigatePages;
    }

    public int[] getNavigatepageNums() {
        return this.navigatepageNums;
    }

    public Paginator() {
        this.pageNum = 1;
        this.pageSize = 10;
    }

    public Paginator(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Paginator(Page page) {
        this(page, 10);
    }

    public Paginator(Page page, int navigatePages) {
        this.isFirstPage = false;
        this.isLastPage = false;
        this.hasPreviousPage = false;
        this.hasNextPage = false;
        if (page instanceof Page) {
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            if (this.pageSize == 0) {
                this.startRow = 0;
                this.endRow = 0;
            } else if (this.pageNum < this.pages) {
                this.startRow = page.getStartRow() + 1;
                this.endRow = (this.startRow - 1) + this.pageSize;
            } else {
                this.startRow = page.getStartRow() + 1;
                this.endRow = (int) page.getTotal();
            }
            this.navigatePages = navigatePages;
            calcNavigatepageNums();
            calcPage();
            judgePageBoudary();
        }
    }

    private void calcNavigatepageNums() {
        if (this.pages <= this.navigatePages) {
            this.navigatepageNums = new int[this.pages];
            for (int startNum = 0; startNum < this.pages; startNum++) {
                this.navigatepageNums[startNum] = startNum + 1;
            }
            return;
        }
        this.navigatepageNums = new int[this.navigatePages];
        int startNum2 = this.pageNum - (this.navigatePages / 2);
        int endNum = this.pageNum + (this.navigatePages / 2);
        if (startNum2 < 1) {
            int startNum3 = 1;
            for (int i = 0; i < this.navigatePages; i++) {
                int i2 = startNum3;
                startNum3++;
                this.navigatepageNums[i] = i2;
            }
            return;
        }
        if (endNum > this.pages) {
            int endNum2 = this.pages;
            for (int i3 = this.navigatePages - 1; i3 >= 0; i3--) {
                int i4 = endNum2;
                endNum2--;
                this.navigatepageNums[i3] = i4;
            }
            return;
        }
        for (int i5 = 0; i5 < this.navigatePages; i5++) {
            int i6 = startNum2;
            startNum2++;
            this.navigatepageNums[i5] = i6;
        }
    }

    private void calcPage() {
        if (this.navigatepageNums != null && this.navigatepageNums.length > 0) {
            this.firstPage = this.navigatepageNums[0];
            this.lastPage = this.navigatepageNums[this.navigatepageNums.length - 1];
            if (this.pageNum > 1) {
                this.prePage = this.pageNum - 1;
            }
            if (this.pageNum < this.pages) {
                this.nextPage = this.pageNum + 1;
            }
        }
    }

    private void judgePageBoudary() {
        this.isFirstPage = this.pageNum == 1;
        this.isLastPage = this.pageNum == this.pages;
        this.hasPreviousPage = this.pageNum > 1;
        this.hasNextPage = this.pageNum < this.pages;
    }

    public void init(Long count, Integer rowNum, Integer navigatePages) {
        if (navigatePages == null) {
            this.navigatePages = 10;
        }
        this.total = count.longValue();
        this.pages = (int) ((count.longValue() / this.pageSize) + serialVersionUID);
        this.navigatepageNums = new int[this.pages];
        for (int i = 0; i < this.pages; i++) {
            this.navigatepageNums[i] = i + 1;
        }
        if (rowNum.intValue() == 0) {
            this.startRow = 0;
            this.endRow = 0;
        } else {
            this.startRow = 1;
            this.endRow = (this.startRow + rowNum.intValue()) - 1;
        }
        judgePageBoudary();
        calcPage();
    }

    public static boolean checkPaginator(Paginator paginator) {
        if (paginator == null || paginator.getPageNum() == 0 || paginator.getPageSize() == 0) {
            return false;
        }
        return true;
    }

    public static Paginator initPaginator(List list) {
        Paginator paginator = new Paginator();
        paginator.setTotal(list.size());
        paginator.setPageSize(list.size());
        return paginator;
    }
}
