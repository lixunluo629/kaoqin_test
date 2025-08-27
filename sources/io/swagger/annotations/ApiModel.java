package io.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: swagger-annotations-1.5.3.jar:io/swagger/annotations/ApiModel.class */
public @interface ApiModel {
    String value() default "";

    String description() default "";

    Class<?> parent() default Void.class;

    String discriminator() default "";

    Class<?>[] subTypes() default {};

    String reference() default "";
}
