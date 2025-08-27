package com.google.common.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: guava-18.0.jar:com/google/common/eventbus/SynchronizedEventSubscriber.class */
final class SynchronizedEventSubscriber extends EventSubscriber {
    public SynchronizedEventSubscriber(Object target, Method method) {
        super(target, method);
    }

    @Override // com.google.common.eventbus.EventSubscriber
    public void handleEvent(Object event) throws InvocationTargetException {
        synchronized (this) {
            super.handleEvent(event);
        }
    }
}
