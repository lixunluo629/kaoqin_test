package org.apache.ibatis.executor.loader;

import java.io.ObjectStreamException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/WriteReplaceInterface.class */
public interface WriteReplaceInterface {
    Object writeReplace() throws ObjectStreamException;
}
