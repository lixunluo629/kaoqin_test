package org.junit.runners.model;

import java.lang.annotation.Annotation;

/* loaded from: junit-4.12.jar:org/junit/runners/model/Annotatable.class */
public interface Annotatable {
    Annotation[] getAnnotations();

    <T extends Annotation> T getAnnotation(Class<T> cls);
}
