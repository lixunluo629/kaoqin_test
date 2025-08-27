package org.hyperic.sigar.pager;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.Serializable;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/PageControl.class */
public class PageControl implements Serializable, Cloneable {
    public static final int SIZE_UNLIMITED = -1;
    public static final int SORT_UNSORTED = 0;
    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = 2;
    private int pagenum;
    private int pagesize;
    private int sortorder;
    private int sortattribute;
    private Serializable metaData;

    public PageControl() {
        this.pagenum = 0;
        this.pagesize = -1;
        this.sortorder = 0;
        this.sortattribute = 0;
    }

    public PageControl(int pagenum, int pagesize) {
        this.pagenum = 0;
        this.pagesize = -1;
        this.sortorder = 0;
        this.sortattribute = 0;
        this.pagenum = pagenum;
        this.pagesize = pagesize;
    }

    public PageControl(int pagenum, int pagesize, int sortorder, int sortattribute) {
        this.pagenum = 0;
        this.pagesize = -1;
        this.sortorder = 0;
        this.sortattribute = 0;
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.sortorder = sortorder;
        this.sortattribute = sortattribute;
    }

    public boolean isAscending() {
        return this.sortorder == 1;
    }

    public boolean isDescending() {
        return this.sortorder == 2;
    }

    public static PageControl initDefaults(PageControl pc, int defaultSortAttr) {
        PageControl pc2;
        if (pc == null) {
            pc2 = new PageControl();
        } else {
            pc2 = (PageControl) pc.clone();
        }
        if (pc2.getSortattribute() == 0) {
            pc2.setSortattribute(defaultSortAttr);
        }
        if (pc2.getSortorder() == 0) {
            pc2.setSortorder(1);
        }
        return pc2;
    }

    public int getPagenum() {
        return this.pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getPagesize() {
        return this.pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getSortorder() {
        return this.sortorder;
    }

    public void setSortorder(int sortorder) {
        this.sortorder = sortorder;
    }

    public int getSortattribute() {
        return this.sortattribute;
    }

    public void setSortattribute(int attr) {
        this.sortattribute = attr;
    }

    public Serializable getMetaData() {
        return this.metaData;
    }

    public void getMetaData(Serializable metaData) {
        this.metaData = metaData;
    }

    public int getPageEntityIndex() {
        return this.pagenum * this.pagesize;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("{");
        s.append(new StringBuffer().append("pn=").append(this.pagenum).append(SymbolConstants.SPACE_SYMBOL).toString());
        s.append(new StringBuffer().append("ps=").append(this.pagesize).append(SymbolConstants.SPACE_SYMBOL).toString());
        s.append("so=");
        switch (this.sortorder) {
            case 0:
                s.append("unsorted ");
                break;
            case 1:
                s.append("asc ");
                break;
            case 2:
                s.append("desc");
                break;
            default:
                s.append(' ');
                break;
        }
        s.append(new StringBuffer().append("sa=").append(this.sortattribute).append(SymbolConstants.SPACE_SYMBOL).toString());
        s.append("}");
        return s.toString();
    }

    public Object clone() {
        PageControl res = new PageControl(this.pagenum, this.pagesize, this.sortorder, this.sortattribute);
        res.metaData = this.metaData;
        return res;
    }
}
