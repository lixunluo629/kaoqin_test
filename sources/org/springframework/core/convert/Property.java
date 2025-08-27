package org.springframework.core.convert;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/Property.class */
public final class Property {
    private static Map<Property, Annotation[]> annotationCache = new ConcurrentReferenceHashMap();
    private final Class<?> objectType;
    private final Method readMethod;
    private final Method writeMethod;
    private final String name;
    private final MethodParameter methodParameter;
    private Annotation[] annotations;

    public Property(Class<?> objectType, Method readMethod, Method writeMethod) {
        this(objectType, readMethod, writeMethod, null);
    }

    public Property(Class<?> objectType, Method readMethod, Method writeMethod, String name) {
        this.objectType = objectType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.methodParameter = resolveMethodParameter();
        this.name = name != null ? name : resolveName();
    }

    public Class<?> getObjectType() {
        return this.objectType;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.methodParameter.getParameterType();
    }

    public Method getReadMethod() {
        return this.readMethod;
    }

    public Method getWriteMethod() {
        return this.writeMethod;
    }

    MethodParameter getMethodParameter() {
        return this.methodParameter;
    }

    Annotation[] getAnnotations() {
        if (this.annotations == null) {
            this.annotations = resolveAnnotations();
        }
        return this.annotations;
    }

    private String resolveName() {
        int index;
        if (this.readMethod != null) {
            int index2 = this.readMethod.getName().indexOf(BeanUtil.PREFIX_GETTER_GET);
            if (index2 != -1) {
                index = index2 + 3;
            } else {
                int index3 = this.readMethod.getName().indexOf(BeanUtil.PREFIX_GETTER_IS);
                if (index3 == -1) {
                    throw new IllegalArgumentException("Not a getter method");
                }
                index = index3 + 2;
            }
            return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
        }
        int index4 = this.writeMethod.getName().indexOf("set");
        if (index4 == -1) {
            throw new IllegalArgumentException("Not a setter method");
        }
        return StringUtils.uncapitalize(this.writeMethod.getName().substring(index4 + 3));
    }

    private MethodParameter resolveMethodParameter() {
        MethodParameter read = resolveReadMethodParameter();
        MethodParameter write = resolveWriteMethodParameter();
        if (write == null) {
            if (read == null) {
                throw new IllegalStateException("Property is neither readable nor writeable");
            }
            return read;
        }
        if (read != null) {
            Class<?> readType = read.getParameterType();
            Class<?> writeType = write.getParameterType();
            if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
                return read;
            }
        }
        return write;
    }

    private MethodParameter resolveReadMethodParameter() {
        if (getReadMethod() == null) {
            return null;
        }
        return resolveParameterType(new MethodParameter(getReadMethod(), -1));
    }

    private MethodParameter resolveWriteMethodParameter() {
        if (getWriteMethod() == null) {
            return null;
        }
        return resolveParameterType(new MethodParameter(getWriteMethod(), 0));
    }

    private MethodParameter resolveParameterType(MethodParameter parameter) {
        GenericTypeResolver.resolveParameterType(parameter, getObjectType());
        return parameter;
    }

    private Annotation[] resolveAnnotations() {
        Annotation[] annotations = annotationCache.get(this);
        if (annotations == null) {
            Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<>();
            addAnnotationsToMap(annotationMap, getReadMethod());
            addAnnotationsToMap(annotationMap, getWriteMethod());
            addAnnotationsToMap(annotationMap, getField());
            annotations = (Annotation[]) annotationMap.values().toArray(new Annotation[annotationMap.size()]);
            annotationCache.put(this, annotations);
        }
        return annotations;
    }

    private void addAnnotationsToMap(Map<Class<? extends Annotation>, Annotation> annotationMap, AnnotatedElement object) {
        if (object != null) {
            for (Annotation annotation : object.getAnnotations()) {
                annotationMap.put(annotation.annotationType(), annotation);
            }
        }
    }

    private Field getField() {
        String name = getName();
        if (!StringUtils.hasLength(name)) {
            return null;
        }
        Class<?> declaringClass = declaringClass();
        Field field = ReflectionUtils.findField(declaringClass, name);
        if (field == null) {
            field = ReflectionUtils.findField(declaringClass, StringUtils.uncapitalize(name));
            if (field == null) {
                field = ReflectionUtils.findField(declaringClass, StringUtils.capitalize(name));
            }
        }
        return field;
    }

    private Class<?> declaringClass() {
        if (getReadMethod() != null) {
            return getReadMethod().getDeclaringClass();
        }
        return getWriteMethod().getDeclaringClass();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Property)) {
            return false;
        }
        Property otherProperty = (Property) other;
        return ObjectUtils.nullSafeEquals(this.objectType, otherProperty.objectType) && ObjectUtils.nullSafeEquals(this.name, otherProperty.name) && ObjectUtils.nullSafeEquals(this.readMethod, otherProperty.readMethod) && ObjectUtils.nullSafeEquals(this.writeMethod, otherProperty.writeMethod);
    }

    public int hashCode() {
        return (ObjectUtils.nullSafeHashCode(this.objectType) * 31) + ObjectUtils.nullSafeHashCode(this.name);
    }
}
