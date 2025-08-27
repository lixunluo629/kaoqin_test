package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.PropertyAccessor;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/AnnotationAttributes.class */
public class AnnotationAttributes extends LinkedHashMap<String, Object> {
    private static final String UNKNOWN = "unknown";
    private final Class<? extends Annotation> annotationType;
    private final String displayName;
    boolean validated;

    public AnnotationAttributes() {
        this.validated = false;
        this.annotationType = null;
        this.displayName = UNKNOWN;
    }

    public AnnotationAttributes(int initialCapacity) {
        super(initialCapacity);
        this.validated = false;
        this.annotationType = null;
        this.displayName = UNKNOWN;
    }

    public AnnotationAttributes(Class<? extends Annotation> annotationType) {
        this.validated = false;
        Assert.notNull(annotationType, "'annotationType' must not be null");
        this.annotationType = annotationType;
        this.displayName = annotationType.getName();
    }

    public AnnotationAttributes(String annotationType, ClassLoader classLoader) {
        this.validated = false;
        Assert.notNull(annotationType, "'annotationType' must not be null");
        this.annotationType = getAnnotationType(annotationType, classLoader);
        this.displayName = annotationType;
    }

    private static Class<? extends Annotation> getAnnotationType(String annotationType, ClassLoader classLoader) {
        if (classLoader != null) {
            try {
                return classLoader.loadClass(annotationType);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    public AnnotationAttributes(Map<String, Object> map) {
        super(map);
        this.validated = false;
        this.annotationType = null;
        this.displayName = UNKNOWN;
    }

    public AnnotationAttributes(AnnotationAttributes other) {
        super(other);
        this.validated = false;
        this.annotationType = other.annotationType;
        this.displayName = other.displayName;
        this.validated = other.validated;
    }

    public Class<? extends Annotation> annotationType() {
        return this.annotationType;
    }

    public String getString(String attributeName) {
        return (String) getRequiredAttribute(attributeName, String.class);
    }

    @Deprecated
    public String getAliasedString(String attributeName, Class<? extends Annotation> annotationType, Object annotationSource) {
        return (String) getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, String.class);
    }

    public String[] getStringArray(String attributeName) {
        return (String[]) getRequiredAttribute(attributeName, String[].class);
    }

    @Deprecated
    public String[] getAliasedStringArray(String attributeName, Class<? extends Annotation> annotationType, Object annotationSource) {
        return (String[]) getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, String[].class);
    }

    public boolean getBoolean(String attributeName) {
        return ((Boolean) getRequiredAttribute(attributeName, Boolean.class)).booleanValue();
    }

    public <N extends Number> N getNumber(String attributeName) {
        return (N) getRequiredAttribute(attributeName, Number.class);
    }

    public <E extends Enum<?>> E getEnum(String attributeName) {
        return (E) getRequiredAttribute(attributeName, Enum.class);
    }

    public <T> Class<? extends T> getClass(String attributeName) {
        return (Class) getRequiredAttribute(attributeName, Class.class);
    }

    public Class<?>[] getClassArray(String attributeName) {
        return (Class[]) getRequiredAttribute(attributeName, Class[].class);
    }

    @Deprecated
    public Class<?>[] getAliasedClassArray(String attributeName, Class<? extends Annotation> annotationType, Object annotationSource) {
        return (Class[]) getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, Class[].class);
    }

    public AnnotationAttributes getAnnotation(String attributeName) {
        return (AnnotationAttributes) getRequiredAttribute(attributeName, AnnotationAttributes.class);
    }

    public <A extends Annotation> A getAnnotation(String attributeName, Class<A> annotationType) {
        return (A) getRequiredAttribute(attributeName, annotationType);
    }

    public AnnotationAttributes[] getAnnotationArray(String attributeName) {
        return (AnnotationAttributes[]) getRequiredAttribute(attributeName, AnnotationAttributes[].class);
    }

    public <A extends Annotation> A[] getAnnotationArray(String str, Class<A> cls) {
        return (A[]) ((Annotation[]) getRequiredAttribute(str, Array.newInstance((Class<?>) cls, 0).getClass()));
    }

    private <T> T getRequiredAttribute(String str, Class<T> cls) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Assert.hasText(str, "'attributeName' must not be null or empty");
        Object obj = get(str);
        assertAttributePresence(str, obj);
        assertNotException(str, obj);
        if (!cls.isInstance(obj) && cls.isArray() && cls.getComponentType().isInstance(obj)) {
            Object objNewInstance = Array.newInstance(cls.getComponentType(), 1);
            Array.set(objNewInstance, 0, obj);
            obj = objNewInstance;
        }
        assertAttributeType(str, obj, cls);
        return (T) obj;
    }

    private <T> T getRequiredAttributeWithAlias(String str, Class<? extends Annotation> cls, Object obj, Class<T> cls2) {
        Assert.hasText(str, "'attributeName' must not be null or empty");
        Assert.notNull(cls, "'annotationType' must not be null");
        Assert.notNull(cls2, "'expectedType' must not be null");
        Object attribute = getAttribute(str, cls2);
        List<String> list = AnnotationUtils.getAttributeAliasMap(cls).get(str);
        if (list != null) {
            for (String str2 : list) {
                Object attribute2 = getAttribute(str2, cls2);
                boolean zIsEmpty = ObjectUtils.isEmpty(attribute);
                boolean zIsEmpty2 = ObjectUtils.isEmpty(attribute2);
                if (!zIsEmpty && !zIsEmpty2 && !ObjectUtils.nullSafeEquals(attribute, attribute2)) {
                    throw new AnnotationConfigurationException(String.format("In annotation [%s] declared on [%s], attribute [%s] and its alias [%s] are present with values of [%s] and [%s], but only one is permitted.", cls.getName(), obj == null ? "unknown element" : obj.toString(), str, str2, ObjectUtils.nullSafeToString(attribute), ObjectUtils.nullSafeToString(attribute2)));
                }
                if (cls2.isArray() && attribute == null && attribute2 != null) {
                    attribute = attribute2;
                } else if (zIsEmpty && !zIsEmpty2) {
                    attribute = attribute2;
                }
            }
            assertAttributePresence(str, list, attribute);
        }
        return (T) attribute;
    }

    private <T> T getAttribute(String str, Class<T> cls) {
        T t = (T) get(str);
        if (t != null) {
            assertNotException(str, t);
            assertAttributeType(str, t, cls);
        }
        return t;
    }

    private void assertAttributePresence(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            throw new IllegalArgumentException(String.format("Attribute '%s' not found in attributes for annotation [%s]", attributeName, this.displayName));
        }
    }

    private void assertAttributePresence(String attributeName, List<String> aliases, Object attributeValue) {
        if (attributeValue == null) {
            throw new IllegalArgumentException(String.format("Neither attribute '%s' nor one of its aliases %s was found in attributes for annotation [%s]", attributeName, aliases, this.displayName));
        }
    }

    private void assertNotException(String attributeName, Object attributeValue) {
        if (attributeValue instanceof Exception) {
            throw new IllegalArgumentException(String.format("Attribute '%s' for annotation [%s] was not resolvable due to exception [%s]", attributeName, this.displayName, attributeValue), (Exception) attributeValue);
        }
    }

    private void assertAttributeType(String attributeName, Object attributeValue, Class<?> expectedType) {
        if (!expectedType.isInstance(attributeValue)) {
            throw new IllegalArgumentException(String.format("Attribute '%s' is of type [%s], but [%s] was expected in attributes for annotation [%s]", attributeName, attributeValue.getClass().getSimpleName(), expectedType.getSimpleName(), this.displayName));
        }
    }

    @Override // java.util.HashMap, java.util.Map
    public Object putIfAbsent(String key, Object value) {
        Object obj = get(key);
        if (obj == null) {
            obj = put(key, value);
        }
        return obj;
    }

    @Override // java.util.AbstractMap
    public String toString() {
        Iterator<Map.Entry<String, Object>> entries = entrySet().iterator();
        StringBuilder sb = new StringBuilder("{");
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(valueToString(entry.getValue()));
            sb.append(entries.hasNext() ? ", " : "");
        }
        sb.append("}");
        return sb.toString();
    }

    private String valueToString(Object value) {
        if (value == this) {
            return "(this Map)";
        }
        if (value instanceof Object[]) {
            return PropertyAccessor.PROPERTY_KEY_PREFIX + StringUtils.arrayToDelimitedString((Object[]) value, ", ") + "]";
        }
        return String.valueOf(value);
    }

    public static AnnotationAttributes fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        if (map instanceof AnnotationAttributes) {
            return (AnnotationAttributes) map;
        }
        return new AnnotationAttributes(map);
    }
}
