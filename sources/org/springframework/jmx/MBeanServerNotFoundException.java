package org.springframework.jmx;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/MBeanServerNotFoundException.class */
public class MBeanServerNotFoundException extends JmxException {
    public MBeanServerNotFoundException(String msg) {
        super(msg);
    }

    public MBeanServerNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
