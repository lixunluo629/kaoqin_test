package io.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: swagger-annotations-1.5.3.jar:io/swagger/annotations/Info.class */
public @interface Info {
    String title();

    String version();

    String description() default "";

    String termsOfService() default "";

    Contact contact() default @Contact(name = "");

    License license() default @License(name = "");

    Extension[] extensions() default {@Extension(properties = {@ExtensionProperty(name = "", value = "")})};
}
