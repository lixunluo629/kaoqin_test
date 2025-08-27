package org.springframework.jmx.export;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/UnableToRegisterMBeanException.class */
public class UnableToRegisterMBeanException extends MBeanExportException {
    public UnableToRegisterMBeanException(String msg) {
        super(msg);
    }

    public UnableToRegisterMBeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
