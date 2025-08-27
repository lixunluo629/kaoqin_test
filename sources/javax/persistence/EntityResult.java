package javax.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: persistence-api-1.0.jar:javax/persistence/EntityResult.class */
public @interface EntityResult {
    Class entityClass();

    FieldResult[] fields() default {};

    String discriminatorColumn() default "";
}
