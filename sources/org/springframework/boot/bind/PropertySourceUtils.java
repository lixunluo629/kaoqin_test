package org.springframework.boot.bind;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/PropertySourceUtils.class */
public abstract class PropertySourceUtils {
    public static Map<String, Object> getSubProperties(PropertySources propertySources, String keyPrefix) {
        return getSubProperties(propertySources, null, keyPrefix);
    }

    public static Map<String, Object> getSubProperties(PropertySources propertySources, String rootPrefix, String keyPrefix) {
        RelaxedNames keyPrefixes = new RelaxedNames(keyPrefix);
        Map<String, Object> subProperties = new LinkedHashMap<>();
        for (PropertySource<?> source : propertySources) {
            if (source instanceof EnumerablePropertySource) {
                for (String name : ((EnumerablePropertySource) source).getPropertyNames()) {
                    String key = getSubKey(name, rootPrefix, keyPrefixes);
                    if (key != null && !subProperties.containsKey(key)) {
                        subProperties.put(key, source.getProperty(name));
                    }
                }
            }
        }
        return Collections.unmodifiableMap(subProperties);
    }

    private static String getSubKey(String name, String rootPrefixes, RelaxedNames keyPrefix) {
        Iterator<String> it = new RelaxedNames(rootPrefixes != null ? rootPrefixes : "").iterator();
        while (it.hasNext()) {
            String rootPrefix = it.next();
            Iterator<String> it2 = keyPrefix.iterator();
            while (it2.hasNext()) {
                String candidateKeyPrefix = it2.next();
                if (name.startsWith(rootPrefix + candidateKeyPrefix)) {
                    return name.substring((rootPrefix + candidateKeyPrefix).length());
                }
            }
        }
        return null;
    }
}
