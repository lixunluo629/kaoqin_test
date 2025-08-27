package org.apache.ibatis.executor.loader.cglib;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.ibatis.executor.loader.AbstractEnhancedDeserializationProxy;
import org.apache.ibatis.executor.loader.AbstractSerialStateHolder;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.executor.loader.WriteReplaceInterface;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyCopier;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/cglib/CglibProxyFactory.class */
public class CglibProxyFactory implements ProxyFactory {
    private static final Log log = LogFactory.getLog((Class<?>) CglibProxyFactory.class);
    private static final String FINALIZE_METHOD = "finalize";
    private static final String WRITE_REPLACE_METHOD = "writeReplace";

    public CglibProxyFactory() {
        try {
            Resources.classForName("net.sf.cglib.proxy.Enhancer");
        } catch (Throwable e) {
            throw new IllegalStateException("Cannot enable lazy loading because CGLIB is not available. Add CGLIB to your classpath.", e);
        }
    }

    @Override // org.apache.ibatis.executor.loader.ProxyFactory
    public Object createProxy(Object target, ResultLoaderMap lazyLoader, Configuration configuration, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return EnhancedResultObjectProxyImpl.createProxy(target, lazyLoader, configuration, objectFactory, constructorArgTypes, constructorArgs);
    }

    public Object createDeserializationProxy(Object target, Map<String, ResultLoaderMap.LoadPair> unloadedProperties, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return EnhancedDeserializationProxyImpl.createProxy(target, unloadedProperties, objectFactory, constructorArgTypes, constructorArgs);
    }

    @Override // org.apache.ibatis.executor.loader.ProxyFactory
    public void setProperties(Properties properties) {
    }

    static Object crateProxy(Class<?> type, Callback callback, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) throws NoSuchMethodException, SecurityException {
        Object enhanced;
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(callback);
        enhancer.setSuperclass(type);
        try {
            type.getDeclaredMethod(WRITE_REPLACE_METHOD, new Class[0]);
            if (log.isDebugEnabled()) {
                log.debug("writeReplace method was found on bean " + type + ", make sure it returns this");
            }
        } catch (NoSuchMethodException e) {
            enhancer.setInterfaces(new Class[]{WriteReplaceInterface.class});
        } catch (SecurityException e2) {
        }
        if (constructorArgTypes.isEmpty()) {
            enhanced = enhancer.create();
        } else {
            Class<?>[] typesArray = (Class[]) constructorArgTypes.toArray(new Class[constructorArgTypes.size()]);
            Object[] valuesArray = constructorArgs.toArray(new Object[constructorArgs.size()]);
            enhanced = enhancer.create(typesArray, valuesArray);
        }
        return enhanced;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/cglib/CglibProxyFactory$EnhancedResultObjectProxyImpl.class */
    private static class EnhancedResultObjectProxyImpl implements MethodInterceptor {
        private final Class<?> type;
        private final ResultLoaderMap lazyLoader;
        private final boolean aggressive;
        private final Set<String> lazyLoadTriggerMethods;
        private final ObjectFactory objectFactory;
        private final List<Class<?>> constructorArgTypes;
        private final List<Object> constructorArgs;

        private EnhancedResultObjectProxyImpl(Class<?> type, ResultLoaderMap lazyLoader, Configuration configuration, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
            this.type = type;
            this.lazyLoader = lazyLoader;
            this.aggressive = configuration.isAggressiveLazyLoading();
            this.lazyLoadTriggerMethods = configuration.getLazyLoadTriggerMethods();
            this.objectFactory = objectFactory;
            this.constructorArgTypes = constructorArgTypes;
            this.constructorArgs = constructorArgs;
        }

        public static Object createProxy(Object target, ResultLoaderMap lazyLoader, Configuration configuration, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException {
            Class<?> type = target.getClass();
            EnhancedResultObjectProxyImpl callback = new EnhancedResultObjectProxyImpl(type, lazyLoader, configuration, objectFactory, constructorArgTypes, constructorArgs);
            Object enhanced = CglibProxyFactory.crateProxy(type, callback, constructorArgTypes, constructorArgs);
            PropertyCopier.copyBeanProperties(type, target, enhanced);
            return enhanced;
        }

        @Override // net.sf.cglib.proxy.MethodInterceptor
        public Object intercept(Object enhanced, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object original;
            String methodName = method.getName();
            try {
                synchronized (this.lazyLoader) {
                    if (CglibProxyFactory.WRITE_REPLACE_METHOD.equals(methodName)) {
                        if (this.constructorArgTypes.isEmpty()) {
                            original = this.objectFactory.create(this.type);
                        } else {
                            original = this.objectFactory.create(this.type, this.constructorArgTypes, this.constructorArgs);
                        }
                        PropertyCopier.copyBeanProperties(this.type, enhanced, original);
                        if (this.lazyLoader.size() > 0) {
                            return new CglibSerialStateHolder(original, this.lazyLoader.getProperties(), this.objectFactory, this.constructorArgTypes, this.constructorArgs);
                        }
                        return original;
                    }
                    if (this.lazyLoader.size() > 0 && !CglibProxyFactory.FINALIZE_METHOD.equals(methodName)) {
                        if (this.aggressive || this.lazyLoadTriggerMethods.contains(methodName)) {
                            this.lazyLoader.loadAll();
                        } else if (PropertyNamer.isSetter(methodName)) {
                            this.lazyLoader.remove(PropertyNamer.methodToProperty(methodName));
                        } else if (PropertyNamer.isGetter(methodName)) {
                            String property = PropertyNamer.methodToProperty(methodName);
                            if (this.lazyLoader.hasLoader(property)) {
                                this.lazyLoader.load(property);
                            }
                        }
                    }
                    return methodProxy.invokeSuper(enhanced, args);
                }
            } catch (Throwable t) {
                throw ExceptionUtil.unwrapThrowable(t);
            }
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/cglib/CglibProxyFactory$EnhancedDeserializationProxyImpl.class */
    private static class EnhancedDeserializationProxyImpl extends AbstractEnhancedDeserializationProxy implements MethodInterceptor {
        private EnhancedDeserializationProxyImpl(Class<?> type, Map<String, ResultLoaderMap.LoadPair> unloadedProperties, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
            super(type, unloadedProperties, objectFactory, constructorArgTypes, constructorArgs);
        }

        public static Object createProxy(Object target, Map<String, ResultLoaderMap.LoadPair> unloadedProperties, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException {
            Class<?> type = target.getClass();
            EnhancedDeserializationProxyImpl callback = new EnhancedDeserializationProxyImpl(type, unloadedProperties, objectFactory, constructorArgTypes, constructorArgs);
            Object enhanced = CglibProxyFactory.crateProxy(type, callback, constructorArgTypes, constructorArgs);
            PropertyCopier.copyBeanProperties(type, target, enhanced);
            return enhanced;
        }

        @Override // net.sf.cglib.proxy.MethodInterceptor
        public Object intercept(Object enhanced, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object o = super.invoke(enhanced, method, args);
            return o instanceof AbstractSerialStateHolder ? o : methodProxy.invokeSuper(o, args);
        }

        @Override // org.apache.ibatis.executor.loader.AbstractEnhancedDeserializationProxy
        protected AbstractSerialStateHolder newSerialStateHolder(Object userBean, Map<String, ResultLoaderMap.LoadPair> unloadedProperties, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
            return new CglibSerialStateHolder(userBean, unloadedProperties, objectFactory, constructorArgTypes, constructorArgs);
        }
    }
}
