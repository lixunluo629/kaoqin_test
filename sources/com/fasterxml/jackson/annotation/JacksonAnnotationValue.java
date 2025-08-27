package com.fasterxml.jackson.annotation;

import java.lang.annotation.Annotation;

/* loaded from: jackson-annotations-2.11.2.jar:com/fasterxml/jackson/annotation/JacksonAnnotationValue.class */
public interface JacksonAnnotationValue<A extends Annotation> {
    Class<A> valueFor();
}
