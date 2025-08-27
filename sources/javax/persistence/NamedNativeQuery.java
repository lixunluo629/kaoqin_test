package javax.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: persistence-api-1.0.jar:javax/persistence/NamedNativeQuery.class */
public @interface NamedNativeQuery {
    String name() default "";

    String query();

    QueryHint[] hints() default {};

    Class resultClass() default void.class;

    String resultSetMapping() default "";
}
