package org.apache.poi.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
/* loaded from: poi-3.17.jar:org/apache/poi/util/SuppressForbidden.class */
public @interface SuppressForbidden {
    String value() default "";
}
