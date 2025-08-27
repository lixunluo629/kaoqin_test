package org.springframework.boot.context.embedded;

import org.springframework.context.ApplicationEvent;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/EmbeddedServletContainerInitializedEvent.class */
public class EmbeddedServletContainerInitializedEvent extends ApplicationEvent {
    private final EmbeddedWebApplicationContext applicationContext;

    public EmbeddedServletContainerInitializedEvent(EmbeddedWebApplicationContext applicationContext, EmbeddedServletContainer source) {
        super(source);
        this.applicationContext = applicationContext;
    }

    public EmbeddedServletContainer getEmbeddedServletContainer() {
        return getSource();
    }

    @Override // java.util.EventObject
    public EmbeddedServletContainer getSource() {
        return (EmbeddedServletContainer) super.getSource();
    }

    public EmbeddedWebApplicationContext getApplicationContext() {
        return this.applicationContext;
    }
}
