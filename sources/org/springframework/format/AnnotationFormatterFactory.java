package org.springframework.format;

import java.lang.annotation.Annotation;
import java.util.Set;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/AnnotationFormatterFactory.class */
public interface AnnotationFormatterFactory<A extends Annotation> {
    Set<Class<?>> getFieldTypes();

    Printer<?> getPrinter(A a, Class<?> cls);

    Parser<?> getParser(A a, Class<?> cls);
}
