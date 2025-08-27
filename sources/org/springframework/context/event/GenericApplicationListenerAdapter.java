package org.springframework.context.event;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/GenericApplicationListenerAdapter.class */
public class GenericApplicationListenerAdapter implements GenericApplicationListener, SmartApplicationListener {
    private final ApplicationListener<ApplicationEvent> delegate;
    private final ResolvableType declaredEventType;

    /* JADX WARN: Multi-variable type inference failed */
    public GenericApplicationListenerAdapter(ApplicationListener<?> applicationListener) {
        Assert.notNull(applicationListener, "Delegate listener must not be null");
        this.delegate = applicationListener;
        this.declaredEventType = resolveDeclaredEventType(this.delegate);
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ApplicationEvent event) {
        this.delegate.onApplicationEvent(event);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsEventType(ResolvableType eventType) {
        if (!(this.delegate instanceof SmartApplicationListener)) {
            return this.declaredEventType == null || this.declaredEventType.isAssignableFrom(eventType);
        }
        Class<?> clsResolve = eventType.resolve();
        return clsResolve != null && ((SmartApplicationListener) this.delegate).supportsEventType(clsResolve);
    }

    @Override // org.springframework.context.event.SmartApplicationListener
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return supportsEventType(ResolvableType.forClass(eventType));
    }

    @Override // org.springframework.context.event.GenericApplicationListener
    public boolean supportsSourceType(Class<?> sourceType) {
        return !(this.delegate instanceof SmartApplicationListener) || ((SmartApplicationListener) this.delegate).supportsSourceType(sourceType);
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        if (this.delegate instanceof Ordered) {
            return ((Ordered) this.delegate).getOrder();
        }
        return Integer.MAX_VALUE;
    }

    static ResolvableType resolveDeclaredEventType(Class<?> listenerType) {
        ResolvableType resolvableType = ResolvableType.forClass(listenerType).as(ApplicationListener.class);
        if (resolvableType.hasGenerics()) {
            return resolvableType.getGeneric(new int[0]);
        }
        return null;
    }

    private static ResolvableType resolveDeclaredEventType(ApplicationListener<ApplicationEvent> listener) {
        Class<?> targetClass;
        ResolvableType declaredEventType = resolveDeclaredEventType(listener.getClass());
        if ((declaredEventType == null || declaredEventType.isAssignableFrom(ResolvableType.forClass(ApplicationEvent.class))) && (targetClass = AopUtils.getTargetClass(listener)) != listener.getClass()) {
            declaredEventType = resolveDeclaredEventType(targetClass);
        }
        return declaredEventType;
    }
}
