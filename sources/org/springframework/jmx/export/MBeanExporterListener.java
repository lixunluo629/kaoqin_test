package org.springframework.jmx.export;

import javax.management.ObjectName;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/MBeanExporterListener.class */
public interface MBeanExporterListener {
    void mbeanRegistered(ObjectName objectName);

    void mbeanUnregistered(ObjectName objectName);
}
