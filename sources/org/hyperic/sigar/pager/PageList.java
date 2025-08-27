package org.hyperic.sigar.pager;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/PageList.class */
public class PageList extends ArrayList implements Serializable {
    private int totalSize;
    private boolean isUnbounded;
    private Serializable metaData;

    public PageList() {
        this.totalSize = 0;
        this.isUnbounded = false;
    }

    public PageList(Collection c, int totalSize) {
        super(c);
        this.totalSize = 0;
        this.totalSize = totalSize;
        this.isUnbounded = false;
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuffer s = new StringBuffer("{");
        s.append(new StringBuffer().append("totalSize=").append(this.totalSize).append(SymbolConstants.SPACE_SYMBOL).toString());
        s.append("}");
        return new StringBuffer().append(super.toString()).append(s.toString()).toString();
    }

    public int getTotalSize() {
        return Math.max(size(), this.totalSize);
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public void setMetaData(Serializable metaData) {
        this.metaData = metaData;
    }

    public Serializable getMetaData() {
        return this.metaData;
    }

    public boolean isUnbounded() {
        return this.isUnbounded;
    }

    public void setUnbounded(boolean isUnbounded) {
        this.isUnbounded = isUnbounded;
    }
}
