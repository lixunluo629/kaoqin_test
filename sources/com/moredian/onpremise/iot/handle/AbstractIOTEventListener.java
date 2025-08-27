package com.moredian.onpremise.iot.handle;

import com.moredian.onpremise.iot.IOTHubHandler;
import java.lang.reflect.ParameterizedType;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/handle/AbstractIOTEventListener.class */
public abstract class AbstractIOTEventListener<T> implements IOTEventListener<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void register() {
        IOTHubHandler.getInstance().registry(this);
    }

    @Override // com.moredian.onpremise.iot.handle.IOTEventListener
    public Class<?>[] getSupportedEvents() {
        return new Class[]{getClassGenericType(getClass(), 0)};
    }

    private Class<?> getClassGenericType(Class<?> parentClass, int position) {
        return (Class) ((ParameterizedType) parentClass.getGenericSuperclass()).getActualTypeArguments()[position];
    }
}
