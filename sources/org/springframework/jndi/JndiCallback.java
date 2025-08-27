package org.springframework.jndi;

import javax.naming.Context;
import javax.naming.NamingException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jndi/JndiCallback.class */
public interface JndiCallback<T> {
    T doInContext(Context context) throws NamingException;
}
