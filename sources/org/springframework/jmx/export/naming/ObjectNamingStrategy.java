package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/naming/ObjectNamingStrategy.class */
public interface ObjectNamingStrategy {
    ObjectName getObjectName(Object obj, String str) throws MalformedObjectNameException;
}
