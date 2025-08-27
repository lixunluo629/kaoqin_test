package org.springframework.context.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/event/EventListenerMethodProcessor.class */
public class EventListenerMethodProcessor implements SmartInitializingSingleton, ApplicationContextAware {
    private ConfigurableApplicationContext applicationContext;
    protected final Log logger = LogFactory.getLog(getClass());
    private final EventExpressionEvaluator evaluator = new EventExpressionEvaluator();
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap(64));

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext, "ApplicationContext does not implement ConfigurableApplicationContext");
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override // org.springframework.beans.factory.SmartInitializingSingleton
    public void afterSingletonsInstantiated() {
        List<EventListenerFactory> factories = getEventListenerFactories();
        String[] beanNames = this.applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            if (!ScopedProxyUtils.isScopedTarget(beanName)) {
                Class<?> type = null;
                try {
                    type = AutoProxyUtils.determineTargetClass(this.applicationContext.getBeanFactory(), beanName);
                } catch (Throwable ex) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
                    }
                }
                if (type == null) {
                    continue;
                } else {
                    if (ScopedObject.class.isAssignableFrom(type)) {
                        try {
                            type = AutoProxyUtils.determineTargetClass(this.applicationContext.getBeanFactory(), ScopedProxyUtils.getTargetBeanName(beanName));
                        } catch (Throwable ex2) {
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex2);
                            }
                        }
                    }
                    try {
                        processBean(factories, beanName, type);
                    } catch (Throwable ex3) {
                        throw new BeanInitializationException("Failed to process @EventListener annotation on bean with name '" + beanName + "'", ex3);
                    }
                }
            }
        }
    }

    protected List<EventListenerFactory> getEventListenerFactories() {
        Map<String, EventListenerFactory> beans = this.applicationContext.getBeansOfType(EventListenerFactory.class);
        List<EventListenerFactory> factories = new ArrayList<>(beans.values());
        AnnotationAwareOrderComparator.sort(factories);
        return factories;
    }

    protected void processBean(List<EventListenerFactory> factories, String beanName, Class<?> targetType) {
        if (!this.nonAnnotatedClasses.contains(targetType)) {
            Map<Method, EventListener> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(targetType, new MethodIntrospector.MetadataLookup<EventListener>() { // from class: org.springframework.context.event.EventListenerMethodProcessor.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.springframework.core.MethodIntrospector.MetadataLookup
                    public EventListener inspect(Method method) {
                        return (EventListener) AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class);
                    }
                });
            } catch (Throwable ex) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
                }
            }
            if (CollectionUtils.isEmpty(annotatedMethods)) {
                this.nonAnnotatedClasses.add(targetType);
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("No @EventListener annotations found on bean class: " + targetType.getName());
                    return;
                }
                return;
            }
            for (Method method : annotatedMethods.keySet()) {
                Iterator<EventListenerFactory> it = factories.iterator();
                while (true) {
                    if (it.hasNext()) {
                        EventListenerFactory factory = it.next();
                        if (factory.supportsMethod(method)) {
                            Method methodToUse = AopUtils.selectInvocableMethod(method, this.applicationContext.getType(beanName));
                            ApplicationListener<?> applicationListener = factory.createApplicationListener(beanName, targetType, methodToUse);
                            if (applicationListener instanceof ApplicationListenerMethodAdapter) {
                                ((ApplicationListenerMethodAdapter) applicationListener).init(this.applicationContext, this.evaluator);
                            }
                            this.applicationContext.addApplicationListener(applicationListener);
                        }
                    }
                }
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(annotatedMethods.size() + " @EventListener methods processed on bean '" + beanName + "': " + annotatedMethods);
            }
        }
    }
}
