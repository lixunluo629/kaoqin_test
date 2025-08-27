package org.springframework.transaction.event;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/event/TransactionalEventListenerFactory.class */
public class TransactionalEventListenerFactory implements EventListenerFactory, Ordered {
    private int order = 50;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.context.event.EventListenerFactory
    public boolean supportsMethod(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, TransactionalEventListener.class);
    }

    @Override // org.springframework.context.event.EventListenerFactory
    public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
        return new ApplicationListenerMethodTransactionalAdapter(beanName, type, method);
    }
}
