package org.ehcache.sizeof.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/annotations/IgnoreSizeOf.class */
public @interface IgnoreSizeOf {
    boolean inherited() default false;
}
