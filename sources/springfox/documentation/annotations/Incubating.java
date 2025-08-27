package springfox.documentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/annotations/Incubating.class */
public @interface Incubating {
    String value() default "2.2.0";
}
