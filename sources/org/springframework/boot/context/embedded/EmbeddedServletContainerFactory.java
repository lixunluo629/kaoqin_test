package org.springframework.boot.context.embedded;

import org.springframework.boot.web.servlet.ServletContextInitializer;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/EmbeddedServletContainerFactory.class */
public interface EmbeddedServletContainerFactory {
    EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... servletContextInitializerArr);
}
