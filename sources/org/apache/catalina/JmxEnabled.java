package org.apache.catalina;

import javax.management.MBeanRegistration;
import javax.management.ObjectName;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/JmxEnabled.class */
public interface JmxEnabled extends MBeanRegistration {
    String getDomain();

    void setDomain(String str);

    ObjectName getObjectName();
}
