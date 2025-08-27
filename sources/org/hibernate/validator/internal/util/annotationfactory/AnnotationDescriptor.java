package org.hibernate.validator.internal.util.annotationfactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/annotationfactory/AnnotationDescriptor.class */
public class AnnotationDescriptor<T extends Annotation> {
    private final Class<T> type;
    private final Map<String, Object> elements = new HashMap();

    public static <S extends Annotation> AnnotationDescriptor<S> getInstance(Class<S> annotationType) {
        return new AnnotationDescriptor<>(annotationType);
    }

    public static <S extends Annotation> AnnotationDescriptor<S> getInstance(Class<S> annotationType, Map<String, Object> elements) {
        return new AnnotationDescriptor<>(annotationType, elements);
    }

    public AnnotationDescriptor(Class<T> annotationType) {
        this.type = annotationType;
    }

    public AnnotationDescriptor(Class<T> annotationType, Map<String, Object> elements) {
        this.type = annotationType;
        for (Map.Entry<String, Object> entry : elements.entrySet()) {
            this.elements.put(entry.getKey(), entry.getValue());
        }
    }

    public void setValue(String elementName, Object value) {
        this.elements.put(elementName, value);
    }

    public Object valueOf(String elementName) {
        return this.elements.get(elementName);
    }

    public boolean containsElement(String elementName) {
        return this.elements.containsKey(elementName);
    }

    public int numberOfElements() {
        return this.elements.size();
    }

    public Map<String, Object> getElements() {
        return new HashMap(this.elements);
    }

    public Class<T> type() {
        return this.type;
    }
}
