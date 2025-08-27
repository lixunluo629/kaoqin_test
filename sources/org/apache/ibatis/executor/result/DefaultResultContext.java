package org.apache.ibatis.executor.result;

import org.apache.ibatis.session.ResultContext;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/result/DefaultResultContext.class */
public class DefaultResultContext<T> implements ResultContext<T> {
    private T resultObject = null;
    private int resultCount = 0;
    private boolean stopped = false;

    @Override // org.apache.ibatis.session.ResultContext
    public T getResultObject() {
        return this.resultObject;
    }

    @Override // org.apache.ibatis.session.ResultContext
    public int getResultCount() {
        return this.resultCount;
    }

    @Override // org.apache.ibatis.session.ResultContext
    public boolean isStopped() {
        return this.stopped;
    }

    public void nextResultObject(T resultObject) {
        this.resultCount++;
        this.resultObject = resultObject;
    }

    @Override // org.apache.ibatis.session.ResultContext
    public void stop() {
        this.stopped = true;
    }
}
