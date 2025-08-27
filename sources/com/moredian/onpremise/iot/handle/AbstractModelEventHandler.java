package com.moredian.onpremise.iot.handle;

import java.lang.reflect.ParameterizedType;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/handle/AbstractModelEventHandler.class */
public abstract class AbstractModelEventHandler<T> implements ModelEventHandler<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModelEventListener modelEventListener;

    @PostConstruct
    public void registry() {
        this.modelEventListener.registry(getClassGenericType(getClass(), 0), this);
    }

    private Class<?> getClassGenericType(Class<?> parentClass, int position) {
        return (Class) ((ParameterizedType) parentClass.getGenericSuperclass()).getActualTypeArguments()[position];
    }
}
