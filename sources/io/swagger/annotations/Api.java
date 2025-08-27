package io.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: swagger-annotations-1.5.3.jar:io/swagger/annotations/Api.class */
public @interface Api {
    String value() default "";

    String[] tags() default {""};

    @Deprecated
    String description() default "";

    @Deprecated
    String basePath() default "";

    @Deprecated
    int position() default 0;

    String produces() default "";

    String consumes() default "";

    String protocols() default "";

    Authorization[] authorizations() default {@Authorization("")};

    boolean hidden() default false;
}
