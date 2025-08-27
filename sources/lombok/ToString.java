package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: lombok-1.16.22.jar:lombok/ToString.class */
public @interface ToString {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: lombok-1.16.22.jar:lombok/ToString$Exclude.class */
    public @interface Exclude {
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: lombok-1.16.22.jar:lombok/ToString$Include.class */
    public @interface Include {
        int rank() default 0;

        String name() default "";
    }

    boolean includeFieldNames() default true;

    String[] exclude() default {};

    String[] of() default {};

    boolean callSuper() default false;

    boolean doNotUseGetters() default false;

    boolean onlyExplicitlyIncluded() default false;
}
