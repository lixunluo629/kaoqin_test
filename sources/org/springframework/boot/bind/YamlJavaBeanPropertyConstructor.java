package org.springframework.boot.bind;

import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.NodeId;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/YamlJavaBeanPropertyConstructor.class */
public class YamlJavaBeanPropertyConstructor extends Constructor {
    private final Map<Class<?>, Map<String, Property>> properties;
    private final PropertyUtils propertyUtils;

    public YamlJavaBeanPropertyConstructor(Class<?> theRoot) {
        super((Class<? extends Object>) theRoot);
        this.properties = new HashMap();
        this.propertyUtils = new PropertyUtils();
        this.yamlClassConstructors.put(NodeId.mapping, new CustomPropertyConstructMapping());
    }

    public YamlJavaBeanPropertyConstructor(Class<?> theRoot, Map<Class<?>, Map<String, String>> propertyAliases) {
        this(theRoot);
        for (Class<?> key : propertyAliases.keySet()) {
            Map<String, String> map = propertyAliases.get(key);
            if (map != null) {
                for (String alias : map.keySet()) {
                    addPropertyAlias(alias, key, map.get(alias));
                }
            }
        }
    }

    protected final void addPropertyAlias(String alias, Class<?> type, String name) {
        Map<String, Property> typeMap = this.properties.get(type);
        if (typeMap == null) {
            typeMap = new HashMap();
            this.properties.put(type, typeMap);
        }
        try {
            typeMap.put(alias, this.propertyUtils.getProperty(type, name));
        } catch (IntrospectionException ex) {
            throw new RuntimeException((Throwable) ex);
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/YamlJavaBeanPropertyConstructor$CustomPropertyConstructMapping.class */
    class CustomPropertyConstructMapping extends Constructor.ConstructMapping {
        CustomPropertyConstructMapping() {
            super();
        }

        @Override // org.yaml.snakeyaml.constructor.Constructor.ConstructMapping
        protected Property getProperty(Class<?> type, String name) throws IntrospectionException {
            Map<String, Property> forType = (Map) YamlJavaBeanPropertyConstructor.this.properties.get(type);
            Property property = forType != null ? forType.get(name) : null;
            return property != null ? property : super.getProperty(type, name);
        }
    }
}
