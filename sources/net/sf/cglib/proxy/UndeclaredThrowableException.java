package net.sf.cglib.proxy;

import net.sf.cglib.core.CodeGenerationException;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/UndeclaredThrowableException.class */
public class UndeclaredThrowableException extends CodeGenerationException {
    public UndeclaredThrowableException(Throwable t) {
        super(t);
    }

    public Throwable getUndeclaredThrowable() {
        return getCause();
    }
}
