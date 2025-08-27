package org.springframework.jndi;

import javax.naming.NamingException;
import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jndi/JndiLookupFailureException.class */
public class JndiLookupFailureException extends NestedRuntimeException {
    public JndiLookupFailureException(String msg, NamingException cause) {
        super(msg, cause);
    }
}
