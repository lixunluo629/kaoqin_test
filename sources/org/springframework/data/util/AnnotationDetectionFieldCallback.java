package org.springframework.data.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/AnnotationDetectionFieldCallback.class */
public class AnnotationDetectionFieldCallback implements ReflectionUtils.FieldCallback {
    private final Class<? extends Annotation> annotationType;
    private Field field;

    public AnnotationDetectionFieldCallback(Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType, "AnnotationType must not be null!");
        this.annotationType = annotationType;
    }

    @Override // org.springframework.util.ReflectionUtils.FieldCallback
    public void doWith(Field field) throws IllegalAccessException, IllegalArgumentException {
        if (this.field != null) {
            return;
        }
        Annotation annotation = AnnotatedElementUtils.findMergedAnnotation((AnnotatedElement) field, (Class<Annotation>) this.annotationType);
        if (annotation != null) {
            this.field = field;
            org.springframework.util.ReflectionUtils.makeAccessible(this.field);
        }
    }

    public Class<?> getType() {
        if (this.field == null) {
            return null;
        }
        return this.field.getType();
    }

    public <T> T getValue(Object obj) {
        Assert.notNull(obj, "Source object must not be null!");
        if (this.field == null) {
            return null;
        }
        return (T) org.springframework.util.ReflectionUtils.getField(this.field, obj);
    }
}
