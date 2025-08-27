package io.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: swagger-annotations-1.5.3.jar:io/swagger/annotations/SwaggerDefinition.class */
public @interface SwaggerDefinition {

    /* loaded from: swagger-annotations-1.5.3.jar:io/swagger/annotations/SwaggerDefinition$Scheme.class */
    public enum Scheme {
        DEFAULT,
        HTTP,
        HTTPS,
        WS,
        WSS
    }

    String host() default "";

    String basePath() default "";

    String[] consumes() default {""};

    String[] produces() default {""};

    Scheme[] schemes() default {Scheme.DEFAULT};

    Tag[] tags() default {@Tag(name = "")};

    Info info() default @Info(title = "", version = "");

    ExternalDocs externalDocs() default @ExternalDocs(url = "");
}
