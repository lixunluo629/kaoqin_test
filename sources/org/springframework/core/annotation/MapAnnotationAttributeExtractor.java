package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.ClassUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/MapAnnotationAttributeExtractor.class */
class MapAnnotationAttributeExtractor extends AbstractAliasAwareAnnotationAttributeExtractor<Map<String, Object>> {
    MapAnnotationAttributeExtractor(Map<String, Object> attributes, Class<? extends Annotation> annotationType, AnnotatedElement annotatedElement) {
        super(annotationType, annotatedElement, enrichAndValidateAttributes(attributes, annotationType));
    }

    @Override // org.springframework.core.annotation.AbstractAliasAwareAnnotationAttributeExtractor
    protected Object getRawAttributeValue(Method attributeMethod) {
        return getRawAttributeValue(attributeMethod.getName());
    }

    @Override // org.springframework.core.annotation.AbstractAliasAwareAnnotationAttributeExtractor
    protected Object getRawAttributeValue(String attributeName) {
        return getSource().get(attributeName);
    }

    private static Map<String, Object> enrichAndValidateAttributes(Map<String, Object> originalAttributes, Class<? extends Annotation> annotationType) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Object defaultValue;
        List<String> aliasNames;
        Map<String, Object> attributes = new LinkedHashMap<>(originalAttributes);
        Map<String, List<String>> attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);
        for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotationType)) {
            String attributeName = attributeMethod.getName();
            Object attributeValue = attributes.get(attributeName);
            if (attributeValue == null && (aliasNames = attributeAliasMap.get(attributeName)) != null) {
                Iterator<String> it = aliasNames.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String aliasName = it.next();
                    Object aliasValue = attributes.get(aliasName);
                    if (aliasValue != null) {
                        attributeValue = aliasValue;
                        attributes.put(attributeName, attributeValue);
                        break;
                    }
                }
            }
            if (attributeValue == null && (defaultValue = AnnotationUtils.getDefaultValue(annotationType, attributeName)) != null) {
                attributeValue = defaultValue;
                attributes.put(attributeName, attributeValue);
            }
            if (attributeValue == null) {
                throw new IllegalArgumentException(String.format("Attributes map %s returned null for required attribute '%s' defined by annotation type [%s].", attributes, attributeName, annotationType.getName()));
            }
            Class<?> requiredReturnType = attributeMethod.getReturnType();
            Class<?> cls = attributeValue.getClass();
            if (!ClassUtils.isAssignable(requiredReturnType, cls)) {
                boolean converted = false;
                if (requiredReturnType.isArray() && requiredReturnType.getComponentType() == cls) {
                    Object array = Array.newInstance(requiredReturnType.getComponentType(), 1);
                    Array.set(array, 0, attributeValue);
                    attributes.put(attributeName, array);
                    converted = true;
                } else if (Annotation.class.isAssignableFrom(requiredReturnType) && Map.class.isAssignableFrom(cls)) {
                    Map<String, Object> map = (Map) attributeValue;
                    attributes.put(attributeName, AnnotationUtils.synthesizeAnnotation(map, requiredReturnType, null));
                    converted = true;
                } else if (requiredReturnType.isArray() && cls.isArray() && Annotation.class.isAssignableFrom(requiredReturnType.getComponentType()) && Map.class.isAssignableFrom(cls.getComponentType())) {
                    Class<?> componentType = requiredReturnType.getComponentType();
                    Map<String, Object>[] maps = (Map[]) attributeValue;
                    attributes.put(attributeName, AnnotationUtils.synthesizeAnnotationArray(maps, componentType));
                    converted = true;
                }
                if (!converted) {
                    throw new IllegalArgumentException(String.format("Attributes map %s returned a value of type [%s] for attribute '%s', but a value of type [%s] is required as defined by annotation type [%s].", attributes, cls.getName(), attributeName, requiredReturnType.getName(), annotationType.getName()));
                }
            }
        }
        return attributes;
    }
}
