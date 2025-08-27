package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/naming/SelfNaming.class */
public interface SelfNaming {
    ObjectName getObjectName() throws MalformedObjectNameException;
}
