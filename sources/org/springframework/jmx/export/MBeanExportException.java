package org.springframework.jmx.export;

import org.springframework.jmx.JmxException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/MBeanExportException.class */
public class MBeanExportException extends JmxException {
    public MBeanExportException(String msg) {
        super(msg);
    }

    public MBeanExportException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
