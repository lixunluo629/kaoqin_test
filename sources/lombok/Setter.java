package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: lombok-1.16.22.jar:lombok/Setter.class */
public @interface Setter {

    @Target({})
    @Retention(RetentionPolicy.SOURCE)
    @Deprecated
    /* loaded from: lombok-1.16.22.jar:lombok/Setter$AnyAnnotation.class */
    public @interface AnyAnnotation {
    }

    AccessLevel value() default AccessLevel.PUBLIC;

    AnyAnnotation[] onMethod() default {};

    AnyAnnotation[] onParam() default {};
}
