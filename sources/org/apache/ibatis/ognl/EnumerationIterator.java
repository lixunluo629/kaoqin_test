package org.apache.ibatis.ognl;

import java.util.Enumeration;
import java.util.Iterator;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/EnumerationIterator.class */
public class EnumerationIterator implements Iterator {
    private Enumeration e;

    public EnumerationIterator(Enumeration e) {
        this.e = e;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.e.hasMoreElements();
    }

    @Override // java.util.Iterator
    public Object next() {
        return this.e.nextElement();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("remove() not supported by Enumeration");
    }
}
