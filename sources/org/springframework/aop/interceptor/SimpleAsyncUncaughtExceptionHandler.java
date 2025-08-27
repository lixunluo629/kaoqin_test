package org.springframework.aop.interceptor;

import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/interceptor/SimpleAsyncUncaughtExceptionHandler.class */
public class SimpleAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Log logger = LogFactory.getLog(SimpleAsyncUncaughtExceptionHandler.class);

    @Override // org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (logger.isErrorEnabled()) {
            logger.error("Unexpected error occurred invoking async method: " + method, ex);
        }
    }
}
