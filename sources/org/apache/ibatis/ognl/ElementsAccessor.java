package org.apache.ibatis.ognl;

import java.util.Enumeration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ElementsAccessor.class */
public interface ElementsAccessor {
    Enumeration getElements(Object obj) throws OgnlException;
}
