package org.springframework.plugin.core.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/support/AbstractTypeAwareSupport.class */
public abstract class AbstractTypeAwareSupport<T> implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, InitializingBean {
    private ApplicationContext context;
    private Class<T> type;
    private BeansOfTypeTargetSource targetSource;
    private Collection<Class<?>> exclusions;

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public void setExclusions(Class<?>[] exclusions) {
        this.exclusions = Arrays.asList(exclusions);
    }

    protected List<T> getBeans() {
        ProxyFactory factory = new ProxyFactory((Class<?>) List.class, this.targetSource);
        return (List) factory.getProxy();
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        this.targetSource = new BeansOfTypeTargetSource(this.context, this.type, false, this.exclusions);
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (this.context.equals(event.getApplicationContext())) {
            this.targetSource.freeze();
        }
    }

    /* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/support/AbstractTypeAwareSupport$BeansOfTypeTargetSource.class */
    static class BeansOfTypeTargetSource implements TargetSource {
        private final ListableBeanFactory context;
        private final Class<?> type;
        private final boolean eagerInit;
        private final Collection<Class<?>> exclusions;
        private boolean frozen = false;
        private Collection<Object> components;

        public BeansOfTypeTargetSource(ListableBeanFactory context, Class<?> type, boolean eagerInit, Collection<Class<?>> exclusions) {
            Assert.notNull(context);
            Assert.notNull(type);
            this.context = context;
            this.type = type;
            this.eagerInit = eagerInit;
            this.exclusions = exclusions == null ? Collections.emptySet() : exclusions;
        }

        public void freeze() {
            this.frozen = true;
        }

        @Override // org.springframework.aop.TargetSource, org.springframework.aop.TargetClassAware
        public Class<?> getTargetClass() {
            return List.class;
        }

        @Override // org.springframework.aop.TargetSource
        public boolean isStatic() {
            return this.frozen;
        }

        @Override // org.springframework.aop.TargetSource
        public synchronized Object getTarget() throws Exception {
            Collection<Object> components = this.components == null ? getBeansOfTypeExcept(this.type, this.exclusions) : this.components;
            if (this.frozen && this.components == null) {
                this.components = components;
            }
            return new ArrayList(components);
        }

        @Override // org.springframework.aop.TargetSource
        public void releaseTarget(Object target) throws Exception {
        }

        private Collection<Object> getBeansOfTypeExcept(Class<?> type, Collection<Class<?>> exceptions) {
            List<Object> result = new ArrayList<>();
            for (String beanName : this.context.getBeanNamesForType(type, false, this.eagerInit)) {
                if (!exceptions.contains(this.context.getType(beanName))) {
                    result.add(this.context.getBean(beanName));
                }
            }
            return result;
        }
    }
}
