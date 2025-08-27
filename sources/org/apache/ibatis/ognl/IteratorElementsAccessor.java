package org.apache.ibatis.ognl;

import java.util.Enumeration;
import java.util.Iterator;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/IteratorElementsAccessor.class */
public class IteratorElementsAccessor implements ElementsAccessor {
    @Override // org.apache.ibatis.ognl.ElementsAccessor
    public Enumeration getElements(Object target) {
        return new IteratorEnumeration((Iterator) target);
    }
}
