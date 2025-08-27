package com.fasterxml.jackson.databind.util;

import java.lang.annotation.Annotation;

/* loaded from: jackson-databind-2.11.2.jar:com/fasterxml/jackson/databind/util/Annotations.class */
public interface Annotations {
    <A extends Annotation> A get(Class<A> cls);

    boolean has(Class<?> cls);

    boolean hasOneOf(Class<? extends Annotation>[] clsArr);

    int size();
}
