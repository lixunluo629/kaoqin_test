package com.google.common.eventbus;

/* loaded from: guava-18.0.jar:com/google/common/eventbus/SubscriberExceptionHandler.class */
public interface SubscriberExceptionHandler {
    void handleException(Throwable th, SubscriberExceptionContext subscriberExceptionContext);
}
