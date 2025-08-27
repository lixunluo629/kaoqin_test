package org.springframework.data.redis.connection;

import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/FutureResult.class */
public abstract class FutureResult<T> {
    protected T resultHolder;
    protected boolean status = false;
    protected Converter converter;

    public abstract Object get();

    public FutureResult(T resultHolder) {
        this.resultHolder = resultHolder;
    }

    public FutureResult(T resultHolder, Converter converter) {
        this.resultHolder = resultHolder;
        this.converter = converter;
    }

    public T getResultHolder() {
        return this.resultHolder;
    }

    public Object convert(Object result) {
        if (this.converter != null) {
            return this.converter.convert(result);
        }
        return result;
    }

    public Converter getConverter() {
        return this.converter;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
