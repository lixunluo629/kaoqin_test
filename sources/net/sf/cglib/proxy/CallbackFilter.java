package net.sf.cglib.proxy;

import java.lang.reflect.Method;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/CallbackFilter.class */
public interface CallbackFilter {
    int accept(Method method);

    boolean equals(Object obj);
}
