package org.apache.ibatis.ognl;

import java.util.Collection;
import java.util.Enumeration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/CollectionElementsAccessor.class */
public class CollectionElementsAccessor implements ElementsAccessor {
    @Override // org.apache.ibatis.ognl.ElementsAccessor
    public Enumeration getElements(Object target) {
        return new IteratorEnumeration(((Collection) target).iterator());
    }
}
