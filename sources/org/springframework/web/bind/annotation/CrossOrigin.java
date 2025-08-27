package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/annotation/CrossOrigin.class */
public @interface CrossOrigin {

    @Deprecated
    public static final String[] DEFAULT_ORIGINS = {"*"};

    @Deprecated
    public static final String[] DEFAULT_ALLOWED_HEADERS = {"*"};

    @Deprecated
    public static final boolean DEFAULT_ALLOW_CREDENTIALS = true;

    @Deprecated
    public static final long DEFAULT_MAX_AGE = 1800;

    @AliasFor("origins")
    String[] value() default {};

    @AliasFor("value")
    String[] origins() default {};

    String[] allowedHeaders() default {};

    String[] exposedHeaders() default {};

    RequestMethod[] methods() default {};

    String allowCredentials() default "";

    long maxAge() default -1;
}
