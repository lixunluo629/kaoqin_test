package org.springframework.data.repository.core.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Generated;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.util.AnnotationDetectionMethodCallback;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/EventPublishingRepositoryProxyPostProcessor.class */
public class EventPublishingRepositoryProxyPostProcessor implements RepositoryProxyPostProcessor {
    private final ApplicationEventPublisher publisher;

    @Generated
    public EventPublishingRepositoryProxyPostProcessor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override // org.springframework.data.repository.core.support.RepositoryProxyPostProcessor
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) throws SecurityException, IllegalArgumentException {
        EventPublishingMethod method = EventPublishingMethod.of(repositoryInformation.getDomainType());
        if (method == null) {
            return;
        }
        factory.addAdvice(new EventPublishingMethodInterceptor(method, this.publisher));
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/EventPublishingRepositoryProxyPostProcessor$EventPublishingMethodInterceptor.class */
    static class EventPublishingMethodInterceptor implements MethodInterceptor {
        private final EventPublishingMethod eventMethod;
        private final ApplicationEventPublisher publisher;

        @Generated
        private EventPublishingMethodInterceptor(EventPublishingMethod eventMethod, ApplicationEventPublisher publisher) {
            this.eventMethod = eventMethod;
            this.publisher = publisher;
        }

        @Generated
        public static EventPublishingMethodInterceptor of(EventPublishingMethod eventMethod, ApplicationEventPublisher publisher) {
            return new EventPublishingMethodInterceptor(eventMethod, publisher);
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Object[] arguments = invocation.getArguments();
            Object result = invocation.proceed();
            if (!invocation.getMethod().getName().startsWith("save")) {
                return result;
            }
            Object eventSource = arguments.length == 1 ? arguments[0] : result;
            this.eventMethod.publishEventsFrom(eventSource, this.publisher);
            return result;
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/EventPublishingRepositoryProxyPostProcessor$EventPublishingMethod.class */
    static class EventPublishingMethod {
        private static Map<Class<?>, EventPublishingMethod> CACHE = new ConcurrentReferenceHashMap();
        private static EventPublishingMethod NONE = new EventPublishingMethod(null, null);
        private final Method publishingMethod;
        private final Method clearingMethod;

        @Generated
        public EventPublishingMethod(Method publishingMethod, Method clearingMethod) {
            this.publishingMethod = publishingMethod;
            this.clearingMethod = clearingMethod;
        }

        public static EventPublishingMethod of(Class<?> type) throws SecurityException, IllegalArgumentException {
            Assert.notNull(type, "Type must not be null!");
            EventPublishingMethod eventPublishingMethod = CACHE.get(type);
            if (eventPublishingMethod != null) {
                return eventPublishingMethod.orNull();
            }
            AnnotationDetectionMethodCallback<DomainEvents> publishing = new AnnotationDetectionMethodCallback<>(DomainEvents.class);
            ReflectionUtils.doWithMethods(type, publishing);
            AnnotationDetectionMethodCallback<AfterDomainEventPublication> clearing = new AnnotationDetectionMethodCallback<>(AfterDomainEventPublication.class);
            ReflectionUtils.doWithMethods(type, clearing);
            EventPublishingMethod result = from(publishing, clearing);
            CACHE.put(type, result);
            return result.orNull();
        }

        public void publishEventsFrom(Object object, ApplicationEventPublisher publisher) {
            if (object == null) {
                return;
            }
            for (Object aggregateRoot : asCollection(object)) {
                for (Object event : asCollection(ReflectionUtils.invokeMethod(this.publishingMethod, aggregateRoot))) {
                    publisher.publishEvent(event);
                }
                if (this.clearingMethod != null) {
                    ReflectionUtils.invokeMethod(this.clearingMethod, aggregateRoot);
                }
            }
        }

        private EventPublishingMethod orNull() {
            if (this == NONE) {
                return null;
            }
            return this;
        }

        private static EventPublishingMethod from(AnnotationDetectionMethodCallback<?> publishing, AnnotationDetectionMethodCallback<?> clearing) {
            if (!publishing.hasFoundAnnotation()) {
                return NONE;
            }
            Method eventMethod = publishing.getMethod();
            ReflectionUtils.makeAccessible(eventMethod);
            return new EventPublishingMethod(eventMethod, getClearingMethod(clearing));
        }

        private static Method getClearingMethod(AnnotationDetectionMethodCallback<?> clearing) {
            if (!clearing.hasFoundAnnotation()) {
                return null;
            }
            Method method = clearing.getMethod();
            ReflectionUtils.makeAccessible(method);
            return method;
        }

        private static Collection<Object> asCollection(Object source) {
            if (source == null) {
                return Collections.emptyList();
            }
            return Collection.class.isInstance(source) ? (Collection) source : Arrays.asList(source);
        }
    }
}
