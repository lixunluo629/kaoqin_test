package org.apache.ibatis.session;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/ResultContext.class */
public interface ResultContext<T> {
    T getResultObject();

    int getResultCount();

    boolean isStopped();

    void stop();
}
