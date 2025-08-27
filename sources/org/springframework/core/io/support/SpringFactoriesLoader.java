package org.springframework.core.io.support;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/io/support/SpringFactoriesLoader.class */
public abstract class SpringFactoriesLoader {
    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
    private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);

    public static <T> List<T> loadFactories(Class<T> factoryClass, ClassLoader classLoader) {
        Assert.notNull(factoryClass, "'factoryClass' must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
        }
        List<String> factoryNames = loadFactoryNames(factoryClass, classLoaderToUse);
        if (logger.isTraceEnabled()) {
            logger.trace("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
        }
        ArrayList arrayList = new ArrayList(factoryNames.size());
        for (String factoryName : factoryNames) {
            arrayList.add(instantiateFactory(factoryName, factoryClass, classLoaderToUse));
        }
        AnnotationAwareOrderComparator.sort(arrayList);
        return arrayList;
    }

    public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
        String factoryClassName = factoryClass.getName();
        try {
            Enumeration<URL> urls = classLoader != null ? classLoader.getResources(FACTORIES_RESOURCE_LOCATION) : ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION);
            List<String> result = new ArrayList<>();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
                String propertyValue = properties.getProperty(factoryClassName);
                for (String factoryName : StringUtils.commaDelimitedListToStringArray(propertyValue)) {
                    result.add(factoryName.trim());
                }
            }
            return result;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load factories from location [META-INF/spring.factories]", ex);
        }
    }

    private static <T> T instantiateFactory(String str, Class<T> cls, ClassLoader classLoader) {
        try {
            Class<?> clsForName = ClassUtils.forName(str, classLoader);
            if (!cls.isAssignableFrom(clsForName)) {
                throw new IllegalArgumentException("Class [" + str + "] is not assignable to [" + cls.getName() + "]");
            }
            Constructor<?> declaredConstructor = clsForName.getDeclaredConstructor(new Class[0]);
            ReflectionUtils.makeAccessible(declaredConstructor);
            return (T) declaredConstructor.newInstance(new Object[0]);
        } catch (Throwable th) {
            throw new IllegalArgumentException("Unable to instantiate factory class: " + cls.getName(), th);
        }
    }
}
