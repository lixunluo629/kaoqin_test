package org.apache.ibatis.ognl;

import java.util.Enumeration;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/MapElementsAccessor.class */
public class MapElementsAccessor implements ElementsAccessor {
    @Override // org.apache.ibatis.ognl.ElementsAccessor
    public Enumeration getElements(Object target) {
        return new IteratorEnumeration(((Map) target).values().iterator());
    }
}
