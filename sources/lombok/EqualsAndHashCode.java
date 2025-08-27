package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: lombok-1.16.22.jar:lombok/EqualsAndHashCode.class */
public @interface EqualsAndHashCode {

    @Target({})
    @Retention(RetentionPolicy.SOURCE)
    @Deprecated
    /* loaded from: lombok-1.16.22.jar:lombok/EqualsAndHashCode$AnyAnnotation.class */
    public @interface AnyAnnotation {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: lombok-1.16.22.jar:lombok/EqualsAndHashCode$Exclude.class */
    public @interface Exclude {
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: lombok-1.16.22.jar:lombok/EqualsAndHashCode$Include.class */
    public @interface Include {
        String replaces() default "";
    }

    String[] exclude() default {};

    String[] of() default {};

    boolean callSuper() default false;

    boolean doNotUseGetters() default false;

    AnyAnnotation[] onParam() default {};

    boolean onlyExplicitlyIncluded() default false;
}
