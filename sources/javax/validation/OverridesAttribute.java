package javax.validation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/OverridesAttribute.class */
public @interface OverridesAttribute {

    @Target({ElementType.METHOD})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/OverridesAttribute$List.class */
    public @interface List {
        OverridesAttribute[] value();
    }

    Class<? extends Annotation> constraint();

    String name();

    int constraintIndex() default -1;
}
