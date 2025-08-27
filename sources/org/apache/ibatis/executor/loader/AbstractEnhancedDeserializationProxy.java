package org.apache.ibatis.executor.loader;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyCopier;
import org.apache.ibatis.reflection.property.PropertyNamer;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/AbstractEnhancedDeserializationProxy.class */
public abstract class AbstractEnhancedDeserializationProxy {
    protected static final String FINALIZE_METHOD = "finalize";
    protected static final String WRITE_REPLACE_METHOD = "writeReplace";
    private final Class<?> type;
    private final Map<String, ResultLoaderMap.LoadPair> unloadedProperties;
    private final ObjectFactory objectFactory;
    private final List<Class<?>> constructorArgTypes;
    private final List<Object> constructorArgs;
    private final Object reloadingPropertyLock = new Object();
    private boolean reloadingProperty = false;

    protected abstract AbstractSerialStateHolder newSerialStateHolder(Object obj, Map<String, ResultLoaderMap.LoadPair> map, ObjectFactory objectFactory, List<Class<?>> list, List<Object> list2);

    protected AbstractEnhancedDeserializationProxy(Class<?> type, Map<String, ResultLoaderMap.LoadPair> unloadedProperties, ObjectFactory objectFactory, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        this.type = type;
        this.unloadedProperties = unloadedProperties;
        this.objectFactory = objectFactory;
        this.constructorArgTypes = constructorArgTypes;
        this.constructorArgs = constructorArgs;
    }

    public final Object invoke(Object enhanced, Method method, Object[] args) throws Throwable {
        Object original;
        String methodName = method.getName();
        try {
            if (WRITE_REPLACE_METHOD.equals(methodName)) {
                if (this.constructorArgTypes.isEmpty()) {
                    original = this.objectFactory.create(this.type);
                } else {
                    original = this.objectFactory.create(this.type, this.constructorArgTypes, this.constructorArgs);
                }
                PropertyCopier.copyBeanProperties(this.type, enhanced, original);
                return newSerialStateHolder(original, this.unloadedProperties, this.objectFactory, this.constructorArgTypes, this.constructorArgs);
            }
            synchronized (this.reloadingPropertyLock) {
                if (!FINALIZE_METHOD.equals(methodName) && PropertyNamer.isProperty(methodName) && !this.reloadingProperty) {
                    String property = PropertyNamer.methodToProperty(methodName);
                    String propertyKey = property.toUpperCase(Locale.ENGLISH);
                    if (this.unloadedProperties.containsKey(propertyKey)) {
                        ResultLoaderMap.LoadPair loadPair = this.unloadedProperties.remove(propertyKey);
                        if (loadPair != null) {
                            try {
                                this.reloadingProperty = true;
                                loadPair.load(enhanced);
                                this.reloadingProperty = false;
                            } catch (Throwable th) {
                                this.reloadingProperty = false;
                                throw th;
                            }
                        } else {
                            throw new ExecutorException("An attempt has been made to read a not loaded lazy property '" + property + "' of a disconnected object");
                        }
                    }
                }
            }
            return enhanced;
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }
}
