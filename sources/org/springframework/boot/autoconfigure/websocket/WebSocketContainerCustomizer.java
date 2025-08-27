package org.springframework.boot.autoconfigure.websocket;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/WebSocketContainerCustomizer.class */
public abstract class WebSocketContainerCustomizer<T extends EmbeddedServletContainerFactory> implements EmbeddedServletContainerCustomizer, Ordered {
    protected abstract void doCustomize(T t);

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return 0;
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
    public void customize(ConfigurableEmbeddedServletContainer container) {
        if (getContainerType().isAssignableFrom(container.getClass())) {
            doCustomize((EmbeddedServletContainerFactory) container);
        }
    }

    protected Class<?> getContainerType() {
        return ResolvableType.forClass(WebSocketContainerCustomizer.class, getClass()).resolveGeneric(new int[0]);
    }
}
