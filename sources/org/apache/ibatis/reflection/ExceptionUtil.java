package org.apache.ibatis.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/ExceptionUtil.class */
public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static Throwable unwrapThrowable(Throwable wrapped) {
        Throwable targetException = wrapped;
        while (true) {
            Throwable unwrapped = targetException;
            if (unwrapped instanceof InvocationTargetException) {
                targetException = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                targetException = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }
}
