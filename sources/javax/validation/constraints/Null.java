package javax.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/constraints/Null.class */
public @interface Null {

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/constraints/Null$List.class */
    public @interface List {
        Null[] value();
    }

    String message() default "{javax.validation.constraints.Null.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
