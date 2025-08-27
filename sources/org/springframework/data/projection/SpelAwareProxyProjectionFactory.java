package org.springframework.data.projection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.util.AnnotationDetectionMethodCallback;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/SpelAwareProxyProjectionFactory.class */
public class SpelAwareProxyProjectionFactory extends ProxyProjectionFactory implements BeanFactoryAware {
    private final Map<Class<?>, Boolean> typeCache = new ConcurrentHashMap();
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private BeanFactory beanFactory;

    @Override // org.springframework.data.projection.ProxyProjectionFactory, org.springframework.data.projection.ProjectionFactory
    public /* bridge */ /* synthetic */ List getInputProperties(Class cls) {
        return super.getInputProperties(cls);
    }

    @Override // org.springframework.data.projection.ProxyProjectionFactory, org.springframework.data.projection.ProjectionFactory
    public /* bridge */ /* synthetic */ Object createProjection(Class cls) {
        return super.createProjection(cls);
    }

    @Override // org.springframework.data.projection.ProxyProjectionFactory, org.springframework.data.projection.ProjectionFactory
    public /* bridge */ /* synthetic */ Object createProjection(Class cls, Object obj) {
        return super.createProjection(cls, obj);
    }

    @Override // org.springframework.data.projection.ProxyProjectionFactory
    public /* bridge */ /* synthetic */ void registerMethodInvokerFactory(MethodInterceptorFactory methodInterceptorFactory) {
        super.registerMethodInvokerFactory(methodInterceptorFactory);
    }

    @Override // org.springframework.data.projection.ProxyProjectionFactory, org.springframework.beans.factory.BeanClassLoaderAware
    public /* bridge */ /* synthetic */ void setBeanClassLoader(ClassLoader classLoader) {
        super.setBeanClassLoader(classLoader);
    }

    @Override // org.springframework.data.projection.ProxyProjectionFactory, org.springframework.context.ResourceLoaderAware
    @Deprecated
    public /* bridge */ /* synthetic */ void setResourceLoader(ResourceLoader resourceLoader) {
        super.setResourceLoader(resourceLoader);
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.data.projection.ProxyProjectionFactory
    protected MethodInterceptor postProcessAccessorInterceptor(MethodInterceptor interceptor, Object source, Class<?> projectionType) throws SecurityException, IllegalArgumentException {
        if (!this.typeCache.containsKey(projectionType)) {
            AnnotationDetectionMethodCallback<Value> callback = new AnnotationDetectionMethodCallback<>(Value.class);
            ReflectionUtils.doWithMethods(projectionType, callback);
            this.typeCache.put(projectionType, Boolean.valueOf(callback.hasFoundAnnotation()));
        }
        return this.typeCache.get(projectionType).booleanValue() ? new SpelEvaluatingMethodInterceptor(interceptor, source, this.beanFactory, this.parser, projectionType) : interceptor;
    }

    @Override // org.springframework.data.projection.ProxyProjectionFactory, org.springframework.data.projection.ProjectionFactory
    public ProjectionInformation getProjectionInformation(Class<?> projectionType) {
        return new SpelAwareProjectionInformation(projectionType);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/SpelAwareProxyProjectionFactory$SpelAwareProjectionInformation.class */
    protected static class SpelAwareProjectionInformation extends DefaultProjectionInformation {
        protected SpelAwareProjectionInformation(Class<?> projectionType) {
            super(projectionType);
        }

        @Override // org.springframework.data.projection.DefaultProjectionInformation
        protected boolean isInputProperty(PropertyDescriptor descriptor) {
            Method readMethod;
            return super.isInputProperty(descriptor) && (readMethod = descriptor.getReadMethod()) != null && AnnotationUtils.findAnnotation(readMethod, Value.class) == null;
        }
    }
}
