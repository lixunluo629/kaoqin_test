package org.hyperic.sigar.util;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/IteratorIterator.class */
public class IteratorIterator implements Iterator {
    private int ix = 0;
    private Iterator curr = null;
    private ArrayList iterators = new ArrayList();

    public void add(Iterator iterator) {
        this.iterators.add(iterator);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        int size = this.iterators.size();
        if (this.curr == null) {
            if (size == 0) {
                return false;
            }
            this.curr = (Iterator) this.iterators.get(0);
        }
        if (this.curr.hasNext()) {
            return true;
        }
        this.ix++;
        if (this.ix >= size) {
            return false;
        }
        this.curr = (Iterator) this.iterators.get(this.ix);
        return hasNext();
    }

    @Override // java.util.Iterator
    public Object next() {
        return this.curr.next();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
