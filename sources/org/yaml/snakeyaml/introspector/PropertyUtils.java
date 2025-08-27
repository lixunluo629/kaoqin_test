package org.yaml.snakeyaml.introspector;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.yaml.snakeyaml.error.YAMLException;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/introspector/PropertyUtils.class */
public class PropertyUtils {
    private final Map<Class<?>, Map<String, Property>> propertiesCache = new HashMap();
    private final Map<Class<?>, Set<Property>> readableProperties = new HashMap();
    private BeanAccess beanAccess = BeanAccess.DEFAULT;
    private boolean allowReadOnlyProperties = false;
    private boolean skipMissingProperties = false;

    protected Map<String, Property> getPropertiesMap(Class<?> type, BeanAccess bAccess) throws IntrospectionException {
        if (this.propertiesCache.containsKey(type)) {
            return this.propertiesCache.get(type);
        }
        Map<String, Property> properties = new LinkedHashMap<>();
        boolean inaccessableFieldsExist = false;
        switch (bAccess) {
            case FIELD:
                Class<?> superclass = type;
                while (true) {
                    Class<?> c = superclass;
                    if (c == null) {
                        break;
                    } else {
                        Field[] arr$ = c.getDeclaredFields();
                        for (Field field : arr$) {
                            int modifiers = field.getModifiers();
                            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers) && !properties.containsKey(field.getName())) {
                                properties.put(field.getName(), new FieldProperty(field));
                            }
                        }
                        superclass = c.getSuperclass();
                    }
                }
                break;
            default:
                PropertyDescriptor[] arr$2 = Introspector.getBeanInfo(type).getPropertyDescriptors();
                for (PropertyDescriptor property : arr$2) {
                    Method readMethod = property.getReadMethod();
                    if (readMethod == null || !readMethod.getName().equals("getClass")) {
                        properties.put(property.getName(), new MethodProperty(property));
                    }
                }
                Class<?> superclass2 = type;
                while (true) {
                    Class<?> c2 = superclass2;
                    if (c2 == null) {
                        break;
                    } else {
                        Field[] arr$3 = c2.getDeclaredFields();
                        for (Field field2 : arr$3) {
                            int modifiers2 = field2.getModifiers();
                            if (!Modifier.isStatic(modifiers2) && !Modifier.isTransient(modifiers2)) {
                                if (Modifier.isPublic(modifiers2)) {
                                    properties.put(field2.getName(), new FieldProperty(field2));
                                } else {
                                    inaccessableFieldsExist = true;
                                }
                            }
                        }
                        superclass2 = c2.getSuperclass();
                    }
                }
                break;
        }
        if (properties.isEmpty() && inaccessableFieldsExist) {
            throw new YAMLException("No JavaBean properties found in " + type.getName());
        }
        this.propertiesCache.put(type, properties);
        return properties;
    }

    public Set<Property> getProperties(Class<? extends Object> type) throws IntrospectionException {
        return getProperties(type, this.beanAccess);
    }

    public Set<Property> getProperties(Class<? extends Object> type, BeanAccess bAccess) throws IntrospectionException {
        if (this.readableProperties.containsKey(type)) {
            return this.readableProperties.get(type);
        }
        Set<Property> properties = createPropertySet(type, bAccess);
        this.readableProperties.put(type, properties);
        return properties;
    }

    protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess) throws IntrospectionException {
        Set<Property> properties = new TreeSet<>();
        Collection<Property> props = getPropertiesMap(type, bAccess).values();
        for (Property property : props) {
            if (property.isReadable() && (this.allowReadOnlyProperties || property.isWritable())) {
                properties.add(property);
            }
        }
        return properties;
    }

    public Property getProperty(Class<? extends Object> type, String name) throws IntrospectionException {
        return getProperty(type, name, this.beanAccess);
    }

    public Property getProperty(Class<? extends Object> type, String name, BeanAccess bAccess) throws IntrospectionException {
        Map<String, Property> properties = getPropertiesMap(type, bAccess);
        Property property = properties.get(name);
        if (property == null && this.skipMissingProperties) {
            property = new MissingProperty(name);
        }
        if (property == null || !property.isWritable()) {
            throw new YAMLException("Unable to find property '" + name + "' on class: " + type.getName());
        }
        return property;
    }

    public void setBeanAccess(BeanAccess beanAccess) {
        if (this.beanAccess != beanAccess) {
            this.beanAccess = beanAccess;
            this.propertiesCache.clear();
            this.readableProperties.clear();
        }
    }

    public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
        if (this.allowReadOnlyProperties != allowReadOnlyProperties) {
            this.allowReadOnlyProperties = allowReadOnlyProperties;
            this.readableProperties.clear();
        }
    }

    public void setSkipMissingProperties(boolean skipMissingProperties) {
        if (this.skipMissingProperties != skipMissingProperties) {
            this.skipMissingProperties = skipMissingProperties;
            this.readableProperties.clear();
        }
    }
}
