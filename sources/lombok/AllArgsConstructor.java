package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: lombok-1.16.22.jar:lombok/AllArgsConstructor.class */
public @interface AllArgsConstructor {

    @Target({})
    @Retention(RetentionPolicy.SOURCE)
    @Deprecated
    /* loaded from: lombok-1.16.22.jar:lombok/AllArgsConstructor$AnyAnnotation.class */
    public @interface AnyAnnotation {
    }

    String staticName() default "";

    AnyAnnotation[] onConstructor() default {};

    AccessLevel access() default AccessLevel.PUBLIC;
}
