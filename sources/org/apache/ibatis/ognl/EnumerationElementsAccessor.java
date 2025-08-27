package org.apache.ibatis.ognl;

import java.util.Enumeration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/EnumerationElementsAccessor.class */
public class EnumerationElementsAccessor implements ElementsAccessor {
    @Override // org.apache.ibatis.ognl.ElementsAccessor
    public Enumeration getElements(Object target) {
        return (Enumeration) target;
    }
}
