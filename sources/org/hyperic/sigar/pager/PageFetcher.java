package org.hyperic.sigar.pager;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/PageFetcher.class */
public abstract class PageFetcher {
    public abstract PageList getPage(PageControl pageControl) throws PageFetchException;
}
