package org.springframework.jmx.export;

import javax.management.ObjectName;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/MBeanExportOperations.class */
public interface MBeanExportOperations {
    ObjectName registerManagedResource(Object obj) throws MBeanExportException;

    void registerManagedResource(Object obj, ObjectName objectName) throws MBeanExportException;

    void unregisterManagedResource(ObjectName objectName);
}
