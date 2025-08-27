package org.hyperic.sigar.pager;

import java.util.Arrays;
import java.util.List;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/StaticPageFetcher.class */
public class StaticPageFetcher extends PageFetcher {
    private List data;

    public StaticPageFetcher(String[] data) {
        this.data = Arrays.asList(data);
    }

    public StaticPageFetcher(List data) {
        this.data = data;
    }

    @Override // org.hyperic.sigar.pager.PageFetcher
    public PageList getPage(PageControl control) throws PageFetchException {
        PageList res = new PageList();
        res.setTotalSize(this.data.size());
        if (control.getPagesize() == -1 || control.getPagenum() == -1) {
            res.addAll(this.data);
            return res;
        }
        int startIdx = control.getPageEntityIndex();
        int endIdx = startIdx + control.getPagesize();
        if (startIdx >= this.data.size()) {
            return res;
        }
        if (endIdx > this.data.size()) {
            endIdx = this.data.size();
        }
        res.addAll(this.data.subList(startIdx, endIdx));
        return res;
    }
}
