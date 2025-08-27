package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ResolvableType;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/SourceFilteringListener.class */
public class SourceFilteringListener implements GenericApplicationListener, SmartApplicationListener {
    private final Object source;
    private GenericApplicationListener delegate;

    public SourceFilteringListener(Object source, ApplicationListener<?> delegate) {
        this.source = source;
        this.delegate = delegate instanceof GenericApplicationListener ? (GenericApplicationListener) delegate : new GenericApplicationListenerAdapter(delegate);
    }

    protected SourceFilteringListener(Object source) {
        this.source = source;
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (event.getSource() == this.source) {
            onApplicationEventInternal(event);
        }
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsEventType(ResolvableType eventType) {
        return this.delegate == null || this.delegate.supportsEventType(eventType);
    }

    @Override // org.springframework.context.event.SmartApplicationListener
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return supportsEventType(ResolvableType.forType(eventType));
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType != null && sourceType.isInstance(this.source);
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        if (this.delegate != null) {
            return this.delegate.getOrder();
        }
        return Integer.MAX_VALUE;
    }

    protected void onApplicationEventInternal(ApplicationEvent event) {
        if (this.delegate == null) {
            throw new IllegalStateException("Must specify a delegate object or override the onApplicationEventInternal method");
        }
        this.delegate.onApplicationEvent(event);
    }
}
