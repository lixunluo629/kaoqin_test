package org.terracotta.offheapstore.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)
/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/FindbugsSuppressWarnings.class */
public @interface FindbugsSuppressWarnings {
    String[] value() default {};
}
