package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: lombok-1.16.22.jar:lombok/Getter.class */
public @interface Getter {

    @Target({})
    @Retention(RetentionPolicy.SOURCE)
    @Deprecated
    /* loaded from: lombok-1.16.22.jar:lombok/Getter$AnyAnnotation.class */
    public @interface AnyAnnotation {
    }

    AccessLevel value() default AccessLevel.PUBLIC;

    AnyAnnotation[] onMethod() default {};

    boolean lazy() default false;
}
