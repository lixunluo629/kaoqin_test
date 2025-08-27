package javax.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: persistence-api-1.0.jar:javax/persistence/SqlResultSetMapping.class */
public @interface SqlResultSetMapping {
    String name();

    EntityResult[] entities() default {};

    ColumnResult[] columns() default {};
}
