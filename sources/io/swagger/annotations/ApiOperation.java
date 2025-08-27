package io.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: swagger-annotations-1.5.3.jar:io/swagger/annotations/ApiOperation.class */
public @interface ApiOperation {
    String value();

    String notes() default "";

    String[] tags() default {""};

    Class<?> response() default Void.class;

    String responseContainer() default "";

    String responseReference() default "";

    String httpMethod() default "";

    @Deprecated
    int position() default 0;

    String nickname() default "";

    String produces() default "";

    String consumes() default "";

    String protocols() default "";

    Authorization[] authorizations() default {@Authorization("")};

    boolean hidden() default false;

    ResponseHeader[] responseHeaders() default {@ResponseHeader(name = "", response = Void.class)};

    int code() default 200;

    Extension[] extensions() default {@Extension(properties = {@ExtensionProperty(name = "", value = "")})};
}
