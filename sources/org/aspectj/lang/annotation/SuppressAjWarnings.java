package org.aspectj.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/annotation/SuppressAjWarnings.class
 */
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/annotation/SuppressAjWarnings.class */
public @interface SuppressAjWarnings {
    String[] value() default {""};
}
