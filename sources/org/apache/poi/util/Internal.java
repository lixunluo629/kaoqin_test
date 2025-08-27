package org.apache.poi.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: poi-3.17.jar:org/apache/poi/util/Internal.class */
public @interface Internal {
    String value() default "";

    String since() default "";
}
