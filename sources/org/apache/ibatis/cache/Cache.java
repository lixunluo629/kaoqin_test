package org.apache.ibatis.cache;

import java.util.concurrent.locks.ReadWriteLock;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/Cache.class */
public interface Cache {
    String getId();

    void putObject(Object obj, Object obj2);

    Object getObject(Object obj);

    Object removeObject(Object obj);

    void clear();

    int getSize();

    ReadWriteLock getReadWriteLock();
}
