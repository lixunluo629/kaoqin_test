package org.aspectj.internal.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/internal/lang/annotation/ajcDeclarePrecedence.class
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/internal/lang/annotation/ajcDeclarePrecedence.class */
public @interface ajcDeclarePrecedence {
    String value();
}
