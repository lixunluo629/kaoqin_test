package org.apache.ibatis.javassist;

import java.io.InputStream;
import java.net.URL;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/ClassPath.class */
public interface ClassPath {
    InputStream openClassfile(String str) throws NotFoundException;

    URL find(String str);

    void close();
}
