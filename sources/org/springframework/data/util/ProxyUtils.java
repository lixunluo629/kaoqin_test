package org.springframework.data.util;

import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/ProxyUtils.class */
public final class ProxyUtils {
    private static Map<Class<?>, Class<?>> USER_TYPES = new ConcurrentReferenceHashMap();
    private static final List<ProxyDetector> DETECTORS = SpringFactoriesLoader.loadFactories(ProxyDetector.class, ProxyUtils.class.getClassLoader());

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/ProxyUtils$ProxyDetector.class */
    public interface ProxyDetector {
        Class<?> getUserType(Class<?> cls);
    }

    @Generated
    private ProxyUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        DETECTORS.add(new ProxyDetector() { // from class: org.springframework.data.util.ProxyUtils.1
            @Override // org.springframework.data.util.ProxyUtils.ProxyDetector
            public Class<?> getUserType(Class<?> type) {
                return ClassUtils.getUserClass(type);
            }
        });
    }

    public static Class<?> getUserClass(Class<?> type) {
        Assert.notNull(type, "Type must not be null!");
        Class<?> result = USER_TYPES.get(type);
        if (result != null) {
            return result;
        }
        Class<?> result2 = type;
        for (ProxyDetector proxyDetector : DETECTORS) {
            result2 = proxyDetector.getUserType(result2);
        }
        USER_TYPES.put(type, result2);
        return result2;
    }

    public static Class<?> getUserClass(Object source) {
        Assert.notNull(source, "Source object must not be null!");
        return getUserClass(source.getClass());
    }
}
