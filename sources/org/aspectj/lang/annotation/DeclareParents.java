package org.aspectj.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/annotation/DeclareParents.class
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/annotation/DeclareParents.class */
public @interface DeclareParents {
    String value();

    Class defaultImpl() default DeclareParents.class;
}
