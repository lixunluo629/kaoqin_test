package javax.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: persistence-api-1.0.jar:javax/persistence/TableGenerator.class */
public @interface TableGenerator {
    String name();

    String table() default "";

    String catalog() default "";

    String schema() default "";

    String pkColumnName() default "";

    String valueColumnName() default "";

    String pkColumnValue() default "";

    int initialValue() default 0;

    int allocationSize() default 50;

    UniqueConstraint[] uniqueConstraints() default {};
}
