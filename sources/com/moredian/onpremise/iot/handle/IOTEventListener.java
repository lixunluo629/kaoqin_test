package com.moredian.onpremise.iot.handle;

import com.moredian.onpremise.iot.IOTContext;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/handle/IOTEventListener.class */
public interface IOTEventListener<E> {
    Class<?>[] getSupportedEvents();

    void handleEvent(IOTContext<E> iOTContext) throws Exception;
}
