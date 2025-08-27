package org.springframework.hateoas.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/AnnotationMappingDiscoverer.class */
public class AnnotationMappingDiscoverer implements MappingDiscoverer {
    private static final Pattern MULTIPLE_SLASHES = Pattern.compile("\\/{2,}");
    private final Class<? extends Annotation> annotationType;
    private final String mappingAttributeName;

    public AnnotationMappingDiscoverer(Class<? extends Annotation> annotation) {
        this(annotation, null);
    }

    public AnnotationMappingDiscoverer(Class<? extends Annotation> annotation, String mappingAttributeName) {
        Assert.notNull(annotation);
        this.annotationType = annotation;
        this.mappingAttributeName = mappingAttributeName;
    }

    @Override // org.springframework.hateoas.core.MappingDiscoverer
    public String getMapping(Class<?> type) {
        Assert.notNull(type, "Type must not be null!");
        String[] mapping = getMappingFrom(AnnotatedElementUtils.findMergedAnnotation(type, this.annotationType));
        if (mapping.length == 0) {
            return null;
        }
        return mapping[0];
    }

    @Override // org.springframework.hateoas.core.MappingDiscoverer
    public String getMapping(Method method) {
        Assert.notNull(method, "Method must not be null!");
        return getMapping(method.getDeclaringClass(), method);
    }

    @Override // org.springframework.hateoas.core.MappingDiscoverer
    public String getMapping(Class<?> type, Method method) {
        Assert.notNull(type, "Type must not be null!");
        Assert.notNull(method, "Method must not be null!");
        String[] mapping = getMappingFrom(AnnotatedElementUtils.findMergedAnnotation(method, this.annotationType));
        String typeMapping = getMapping(type);
        if (mapping == null || mapping.length == 0) {
            return typeMapping;
        }
        return (typeMapping == null || "/".equals(typeMapping)) ? mapping[0] : join(typeMapping, mapping[0]);
    }

    private String[] getMappingFrom(Annotation annotation) {
        Object value = this.mappingAttributeName == null ? AnnotationUtils.getValue(annotation) : AnnotationUtils.getValue(annotation, this.mappingAttributeName);
        if (value instanceof String) {
            return new String[]{(String) value};
        }
        if (value instanceof String[]) {
            return (String[]) value;
        }
        if (value == null) {
            return new String[0];
        }
        throw new IllegalStateException(String.format("Unsupported type for the mapping attribute! Support String and String[] but got %s!", value.getClass()));
    }

    private static String join(String typeMapping, String mapping) {
        return MULTIPLE_SLASHES.matcher(typeMapping.concat("/").concat(mapping)).replaceAll("/");
    }
}
