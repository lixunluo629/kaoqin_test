package org.apache.ibatis.ognl.internal;

import org.apache.ibatis.ognl.ClassCacheInspector;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/internal/ClassCache.class */
public interface ClassCache {
    void setClassInspector(ClassCacheInspector classCacheInspector);

    void clear();

    int getSize();

    Object get(Class cls);

    Object put(Class cls, Object obj);
}
