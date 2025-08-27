package javax.validation.executable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/executable/ValidateOnExecution.class */
public @interface ValidateOnExecution {
    ExecutableType[] type() default {ExecutableType.IMPLICIT};
}
