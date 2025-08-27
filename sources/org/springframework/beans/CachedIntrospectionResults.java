package org.springframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.SpringProperties;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/CachedIntrospectionResults.class */
public class CachedIntrospectionResults {
    public static final String IGNORE_BEANINFO_PROPERTY_NAME = "spring.beaninfo.ignore";
    private static final boolean shouldIntrospectorIgnoreBeaninfoClasses = SpringProperties.getFlag(IGNORE_BEANINFO_PROPERTY_NAME);
    private static List<BeanInfoFactory> beanInfoFactories = SpringFactoriesLoader.loadFactories(BeanInfoFactory.class, CachedIntrospectionResults.class.getClassLoader());
    private static final Log logger = LogFactory.getLog(CachedIntrospectionResults.class);
    static final Set<ClassLoader> acceptedClassLoaders = Collections.newSetFromMap(new ConcurrentHashMap(16));
    static final ConcurrentMap<Class<?>, CachedIntrospectionResults> strongClassCache = new ConcurrentHashMap(64);
    static final ConcurrentMap<Class<?>, CachedIntrospectionResults> softClassCache = new ConcurrentReferenceHashMap(64);
    private final BeanInfo beanInfo;
    private final Map<String, PropertyDescriptor> propertyDescriptorCache;
    private final ConcurrentMap<PropertyDescriptor, TypeDescriptor> typeDescriptorCache;

    public static void acceptClassLoader(ClassLoader classLoader) {
        if (classLoader != null) {
            acceptedClassLoaders.add(classLoader);
        }
    }

    public static void clearClassLoader(ClassLoader classLoader) {
        Iterator<ClassLoader> it = acceptedClassLoaders.iterator();
        while (it.hasNext()) {
            ClassLoader registeredLoader = it.next();
            if (isUnderneathClassLoader(registeredLoader, classLoader)) {
                it.remove();
            }
        }
        Iterator<Class<?>> it2 = strongClassCache.keySet().iterator();
        while (it2.hasNext()) {
            Class<?> beanClass = it2.next();
            if (isUnderneathClassLoader(beanClass.getClassLoader(), classLoader)) {
                it2.remove();
            }
        }
        Iterator<Class<?>> it3 = softClassCache.keySet().iterator();
        while (it3.hasNext()) {
            Class<?> beanClass2 = it3.next();
            if (isUnderneathClassLoader(beanClass2.getClassLoader(), classLoader)) {
                it3.remove();
            }
        }
    }

    static CachedIntrospectionResults forClass(Class<?> beanClass) throws BeansException {
        ConcurrentMap<Class<?>, CachedIntrospectionResults> classCacheToUse;
        CachedIntrospectionResults results = strongClassCache.get(beanClass);
        if (results != null) {
            return results;
        }
        CachedIntrospectionResults results2 = softClassCache.get(beanClass);
        if (results2 != null) {
            return results2;
        }
        CachedIntrospectionResults results3 = new CachedIntrospectionResults(beanClass);
        if (ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader()) || isClassLoaderAccepted(beanClass.getClassLoader())) {
            classCacheToUse = strongClassCache;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Not strongly caching class [" + beanClass.getName() + "] because it is not cache-safe");
            }
            classCacheToUse = softClassCache;
        }
        CachedIntrospectionResults existing = classCacheToUse.putIfAbsent(beanClass, results3);
        return existing != null ? existing : results3;
    }

    private static boolean isClassLoaderAccepted(ClassLoader classLoader) {
        for (ClassLoader acceptedLoader : acceptedClassLoaders) {
            if (isUnderneathClassLoader(classLoader, acceptedLoader)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUnderneathClassLoader(ClassLoader candidate, ClassLoader parent) {
        if (candidate == parent) {
            return true;
        }
        if (candidate == null) {
            return false;
        }
        ClassLoader classLoaderToCheck = candidate;
        while (classLoaderToCheck != null) {
            classLoaderToCheck = classLoaderToCheck.getParent();
            if (classLoaderToCheck == parent) {
                return true;
            }
        }
        return false;
    }

    private CachedIntrospectionResults(Class<?> beanClass) throws BeansException {
        BeanInfo beanInfo;
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Getting BeanInfo for class [" + beanClass.getName() + "]");
            }
            BeanInfo beanInfo2 = null;
            for (BeanInfoFactory beanInfoFactory : beanInfoFactories) {
                beanInfo2 = beanInfoFactory.getBeanInfo(beanClass);
                if (beanInfo2 != null) {
                    break;
                }
            }
            if (beanInfo2 == null) {
                if (shouldIntrospectorIgnoreBeaninfoClasses) {
                    beanInfo = Introspector.getBeanInfo(beanClass, 3);
                } else {
                    beanInfo = Introspector.getBeanInfo(beanClass);
                }
                beanInfo2 = beanInfo;
            }
            this.beanInfo = beanInfo2;
            if (logger.isTraceEnabled()) {
                logger.trace("Caching PropertyDescriptors for class [" + beanClass.getName() + "]");
            }
            this.propertyDescriptorCache = new LinkedHashMap();
            PropertyDescriptor[] pds = this.beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                if (Class.class != beanClass || (!"classLoader".equals(pd.getName()) && !"protectionDomain".equals(pd.getName()))) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Found bean property '" + pd.getName() + "'" + (pd.getPropertyType() != null ? " of type [" + pd.getPropertyType().getName() + "]" : "") + (pd.getPropertyEditorClass() != null ? "; editor [" + pd.getPropertyEditorClass().getName() + "]" : ""));
                    }
                    PropertyDescriptor pd2 = buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
                    this.propertyDescriptorCache.put(pd2.getName(), pd2);
                }
            }
            for (Class<?> clazz = beanClass; clazz != null; clazz = clazz.getSuperclass()) {
                Class<?>[] ifcs = clazz.getInterfaces();
                for (Class<?> ifc : ifcs) {
                    BeanInfo ifcInfo = Introspector.getBeanInfo(ifc, 3);
                    PropertyDescriptor[] ifcPds = ifcInfo.getPropertyDescriptors();
                    for (PropertyDescriptor pd3 : ifcPds) {
                        if (!this.propertyDescriptorCache.containsKey(pd3.getName())) {
                            PropertyDescriptor pd4 = buildGenericTypeAwarePropertyDescriptor(beanClass, pd3);
                            this.propertyDescriptorCache.put(pd4.getName(), pd4);
                        }
                    }
                }
            }
            this.typeDescriptorCache = new ConcurrentReferenceHashMap();
        } catch (IntrospectionException ex) {
            throw new FatalBeanException("Failed to obtain BeanInfo for class [" + beanClass.getName() + "]", ex);
        }
    }

    BeanInfo getBeanInfo() {
        return this.beanInfo;
    }

    Class<?> getBeanClass() {
        return this.beanInfo.getBeanDescriptor().getBeanClass();
    }

    PropertyDescriptor getPropertyDescriptor(String name) {
        PropertyDescriptor pd = this.propertyDescriptorCache.get(name);
        if (pd == null && StringUtils.hasLength(name)) {
            pd = this.propertyDescriptorCache.get(StringUtils.uncapitalize(name));
            if (pd == null) {
                pd = this.propertyDescriptorCache.get(StringUtils.capitalize(name));
            }
        }
        return (pd == null || (pd instanceof GenericTypeAwarePropertyDescriptor)) ? pd : buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd);
    }

    PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pds = new PropertyDescriptor[this.propertyDescriptorCache.size()];
        int i = 0;
        for (PropertyDescriptor pd : this.propertyDescriptorCache.values()) {
            pds[i] = pd instanceof GenericTypeAwarePropertyDescriptor ? pd : buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd);
            i++;
        }
        return pds;
    }

    private PropertyDescriptor buildGenericTypeAwarePropertyDescriptor(Class<?> beanClass, PropertyDescriptor pd) {
        try {
            return new GenericTypeAwarePropertyDescriptor(beanClass, pd.getName(), pd.getReadMethod(), pd.getWriteMethod(), pd.getPropertyEditorClass());
        } catch (IntrospectionException ex) {
            throw new FatalBeanException("Failed to re-introspect class [" + beanClass.getName() + "]", ex);
        }
    }

    TypeDescriptor addTypeDescriptor(PropertyDescriptor pd, TypeDescriptor td) {
        TypeDescriptor existing = this.typeDescriptorCache.putIfAbsent(pd, td);
        return existing != null ? existing : td;
    }

    TypeDescriptor getTypeDescriptor(PropertyDescriptor pd) {
        return this.typeDescriptorCache.get(pd);
    }
}
