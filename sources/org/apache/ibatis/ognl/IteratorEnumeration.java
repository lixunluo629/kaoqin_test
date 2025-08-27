package org.apache.ibatis.ognl;

import java.util.Enumeration;
import java.util.Iterator;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/IteratorEnumeration.class */
public class IteratorEnumeration implements Enumeration {
    private Iterator it;

    public IteratorEnumeration(Iterator it) {
        this.it = it;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return this.it.hasNext();
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        return this.it.next();
    }
}
