package org.aspectj.lang.annotation.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/annotation/control/CodeGenerationHint.class
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/annotation/control/CodeGenerationHint.class */
public @interface CodeGenerationHint {
    String ifNameSuffix() default "";
}
