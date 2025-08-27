package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: lombok-1.16.22.jar:lombok/experimental/FieldNameConstants.class */
public @interface FieldNameConstants {
    AccessLevel level() default AccessLevel.PUBLIC;
}
