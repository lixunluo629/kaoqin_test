package org.springframework.data.projection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/ProxyProjectionFactory.class */
class ProxyProjectionFactory implements ProjectionFactory, ResourceLoaderAware, BeanClassLoaderAware {
    private static final boolean IS_JAVA_8 = ClassUtils.isPresent("java.util.Optional", ProxyProjectionFactory.class.getClassLoader());
    private final List<MethodInterceptorFactory> factories = new ArrayList();
    private final ConversionService conversionService;
    private ClassLoader classLoader;

    protected ProxyProjectionFactory() {
        this.factories.add(MapAccessingMethodInterceptorFactory.INSTANCE);
        this.factories.add(PropertyAccessingMethodInvokerFactory.INSTANCE);
        this.conversionService = new DefaultConversionService();
    }

    @Override // org.springframework.context.ResourceLoaderAware
    @Deprecated
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.classLoader = resourceLoader.getClassLoader();
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void registerMethodInvokerFactory(MethodInterceptorFactory factory) {
        Assert.notNull(factory, "MethodInterceptorFactory must not be null!");
        this.factories.add(0, factory);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.projection.ProjectionFactory
    public <T> T createProjection(Class<T> cls, Object obj) {
        Assert.notNull(cls, "Projection type must not be null!");
        Assert.isTrue(cls.isInterface(), "Projection type must be an interface!");
        if (obj == 0 || cls.isInstance(obj)) {
            return obj;
        }
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(obj);
        proxyFactory.setOpaque(true);
        proxyFactory.setInterfaces(cls, TargetAware.class);
        if (IS_JAVA_8) {
            proxyFactory.addAdvice(new DefaultMethodInvokingMethodInterceptor());
        }
        proxyFactory.addAdvice(new TargetAwareMethodInterceptor(obj.getClass()));
        proxyFactory.addAdvice(getMethodInterceptor(obj, cls));
        return (T) proxyFactory.getProxy(this.classLoader == null ? ClassUtils.getDefaultClassLoader() : this.classLoader);
    }

    @Override // org.springframework.data.projection.ProjectionFactory
    public <T> T createProjection(Class<T> cls) {
        Assert.notNull(cls, "Projection type must not be null!");
        return (T) createProjection(cls, new HashMap());
    }

    @Override // org.springframework.data.projection.ProjectionFactory
    public List<String> getInputProperties(Class<?> projectionType) {
        Assert.notNull(projectionType, "Projection type must not be null!");
        List<String> result = new ArrayList<>();
        for (PropertyDescriptor descriptor : getProjectionInformation(projectionType).getInputProperties()) {
            result.add(descriptor.getName());
        }
        return result;
    }

    @Override // org.springframework.data.projection.ProjectionFactory
    public ProjectionInformation getProjectionInformation(Class<?> projectionType) {
        return new DefaultProjectionInformation(projectionType);
    }

    private MethodInterceptor getMethodInterceptor(Object source, Class<?> projectionType) {
        MethodInterceptor propertyInvocationInterceptor = getFactoryFor(source, projectionType).createMethodInterceptor(source, projectionType);
        return new ProjectingMethodInterceptor(this, postProcessAccessorInterceptor(propertyInvocationInterceptor, source, projectionType), this.conversionService);
    }

    private MethodInterceptorFactory getFactoryFor(Object source, Class<?> projectionType) {
        for (MethodInterceptorFactory factory : this.factories) {
            if (factory.supports(source, projectionType)) {
                return factory;
            }
        }
        throw new IllegalStateException("No MethodInterceptorFactory found for type ".concat(source.getClass().getName()));
    }

    protected MethodInterceptor postProcessAccessorInterceptor(MethodInterceptor interceptor, Object source, Class<?> projectionType) {
        return interceptor;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/ProxyProjectionFactory$TargetAwareMethodInterceptor.class */
    private static class TargetAwareMethodInterceptor implements MethodInterceptor {
        private static final Method GET_TARGET_CLASS_METHOD;
        private static final Method GET_TARGET_METHOD;
        private final Class<?> targetType;

        static {
            try {
                GET_TARGET_CLASS_METHOD = TargetAware.class.getMethod("getTargetClass", new Class[0]);
                GET_TARGET_METHOD = TargetAware.class.getMethod("getTarget", new Class[0]);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }

        public TargetAwareMethodInterceptor(Class<?> targetType) {
            Assert.notNull(targetType, "Target type must not be null!");
            this.targetType = targetType;
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (invocation.getMethod().equals(GET_TARGET_CLASS_METHOD)) {
                return this.targetType;
            }
            if (invocation.getMethod().equals(GET_TARGET_METHOD)) {
                return invocation.getThis();
            }
            return invocation.proceed();
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/ProxyProjectionFactory$MapAccessingMethodInterceptorFactory.class */
    private enum MapAccessingMethodInterceptorFactory implements MethodInterceptorFactory {
        INSTANCE;

        @Override // org.springframework.data.projection.MethodInterceptorFactory
        public MethodInterceptor createMethodInterceptor(Object source, Class<?> targetType) {
            return new MapAccessingMethodInterceptor((Map) source);
        }

        @Override // org.springframework.data.projection.MethodInterceptorFactory
        public boolean supports(Object source, Class<?> targetType) {
            return Map.class.isInstance(source);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/ProxyProjectionFactory$PropertyAccessingMethodInvokerFactory.class */
    private enum PropertyAccessingMethodInvokerFactory implements MethodInterceptorFactory {
        INSTANCE;

        @Override // org.springframework.data.projection.MethodInterceptorFactory
        public MethodInterceptor createMethodInterceptor(Object source, Class<?> targetType) {
            return new PropertyAccessingMethodInterceptor(source);
        }

        @Override // org.springframework.data.projection.MethodInterceptorFactory
        public boolean supports(Object source, Class<?> targetType) {
            return true;
        }
    }
}
