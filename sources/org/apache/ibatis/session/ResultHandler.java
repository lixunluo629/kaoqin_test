package org.apache.ibatis.session;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/ResultHandler.class */
public interface ResultHandler<T> {
    void handleResult(ResultContext<? extends T> resultContext);
}
