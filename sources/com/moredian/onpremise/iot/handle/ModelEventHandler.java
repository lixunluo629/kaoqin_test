package com.moredian.onpremise.iot.handle;

import com.moredian.onpremise.iot.IOTContext;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/handle/ModelEventHandler.class */
public interface ModelEventHandler<T> {
    void handle(T t, IOTContext iOTContext);
}
