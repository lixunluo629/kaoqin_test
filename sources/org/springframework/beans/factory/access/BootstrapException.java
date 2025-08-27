package org.springframework.beans.factory.access;

import org.springframework.beans.FatalBeanException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/access/BootstrapException.class */
public class BootstrapException extends FatalBeanException {
    public BootstrapException(String msg) {
        super(msg);
    }

    public BootstrapException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
