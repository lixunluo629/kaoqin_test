package com.moredian.onpremise.core.utils;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;

@ApiModel
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/PageList.class */
public class PageList<T> implements Serializable {
    private Paginator paginator;
    private List<T> list;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PageList)) {
            return false;
        }
        PageList<?> other = (PageList) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        if (this$paginator == null) {
            if (other$paginator != null) {
                return false;
            }
        } else if (!this$paginator.equals(other$paginator)) {
            return false;
        }
        Object this$list = getList();
        Object other$list = other.getList();
        return this$list == null ? other$list == null : this$list.equals(other$list);
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageList;
    }

    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $list = getList();
        return (result * 59) + ($list == null ? 43 : $list.hashCode());
    }

    public String toString() {
        return "PageList(paginator=" + getPaginator() + ", list=" + getList() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public List<T> getList() {
        return this.list;
    }

    public PageList(Paginator paginator, List<T> list) {
        this.paginator = paginator;
        this.list = list;
    }

    public PageList(List<T> list) {
        if (list instanceof Page) {
            this.paginator = new Paginator((Page) list);
            this.list = list;
        }
    }
}
