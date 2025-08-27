package org.hyperic.sigar.pager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/ListPageFetcher.class */
public class ListPageFetcher extends PageFetcher {
    private List data;
    private int sortOrder = 0;

    /* renamed from: org.hyperic.sigar.pager.ListPageFetcher$1, reason: invalid class name */
    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/ListPageFetcher$1.class */
    static class AnonymousClass1 {
    }

    public ListPageFetcher(List data) {
        this.data = data;
    }

    @Override // org.hyperic.sigar.pager.PageFetcher
    public PageList getPage(PageControl control) {
        int endIdx;
        PageList res = new PageList();
        if (this.data.size() == 0) {
            return new PageList();
        }
        ensureSortOrder(control);
        res.setTotalSize(this.data.size());
        int startIdx = clamp(control.getPageEntityIndex(), 0, this.data.size() - 1);
        if (control.getPagesize() == -1) {
            endIdx = this.data.size();
        } else {
            endIdx = clamp(startIdx + control.getPagesize(), startIdx, this.data.size());
        }
        ListIterator i = this.data.listIterator(startIdx);
        for (int curIdx = startIdx; i.hasNext() && curIdx < endIdx; curIdx++) {
            res.add(i.next());
        }
        return res;
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/pager/ListPageFetcher$DescSorter.class */
    private class DescSorter implements Comparator {
        private final ListPageFetcher this$0;

        private DescSorter(ListPageFetcher listPageFetcher) {
            this.this$0 = listPageFetcher;
        }

        DescSorter(ListPageFetcher x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // java.util.Comparator
        public int compare(Object o1, Object o2) {
            return -((Comparable) o1).compareTo((Comparable) o2);
        }

        @Override // java.util.Comparator
        public boolean equals(Object other) {
            return false;
        }
    }

    private void ensureSortOrder(PageControl control) {
        if (control.getSortorder() == this.sortOrder) {
            return;
        }
        this.sortOrder = control.getSortorder();
        if (this.sortOrder == 0) {
            return;
        }
        if (this.sortOrder == 1) {
            Collections.sort(this.data);
        } else {
            if (this.sortOrder == 2) {
                Collections.sort(this.data, new DescSorter(this, null));
                return;
            }
            throw new IllegalStateException(new StringBuffer().append("Unknown control sorting type: ").append(this.sortOrder).toString());
        }
    }

    private static int clamp(int val, int min, int max) {
        return (int) clamp(val, min, max);
    }

    private static long clamp(long val, long min, long max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }
}
