package org.apache.commons.lang;

import org.apache.commons.lang.exception.NestableRuntimeException;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/UnhandledException.class */
public class UnhandledException extends NestableRuntimeException {
    private static final long serialVersionUID = 1832101364842773720L;

    public UnhandledException(Throwable cause) {
        super(cause);
    }

    public UnhandledException(String message, Throwable cause) {
        super(message, cause);
    }
}
