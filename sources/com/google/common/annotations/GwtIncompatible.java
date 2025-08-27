package com.google.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@GwtCompatible
@Retention(RetentionPolicy.CLASS)
@Documented
/* loaded from: guava-18.0.jar:com/google/common/annotations/GwtIncompatible.class */
public @interface GwtIncompatible {
    String value();
}
