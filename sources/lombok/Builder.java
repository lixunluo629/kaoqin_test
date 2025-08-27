package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: lombok-1.16.22.jar:lombok/Builder.class */
public @interface Builder {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: lombok-1.16.22.jar:lombok/Builder$Default.class */
    public @interface Default {
    }

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: lombok-1.16.22.jar:lombok/Builder$ObtainVia.class */
    public @interface ObtainVia {
        String field() default "";

        String method() default "";

        boolean isStatic() default false;
    }

    String builderMethodName() default "builder";

    String buildMethodName() default "build";

    String builderClassName() default "";

    boolean toBuilder() default false;
}
