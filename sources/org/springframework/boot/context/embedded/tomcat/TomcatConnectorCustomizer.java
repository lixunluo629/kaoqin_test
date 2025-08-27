package org.springframework.boot.context.embedded.tomcat;

import org.apache.catalina.connector.Connector;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatConnectorCustomizer.class */
public interface TomcatConnectorCustomizer {
    void customize(Connector connector);
}
