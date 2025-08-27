package org.springframework.boot.context.embedded.tomcat;

import org.apache.catalina.Context;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatContextCustomizer.class */
public interface TomcatContextCustomizer {
    void customize(Context context);
}
