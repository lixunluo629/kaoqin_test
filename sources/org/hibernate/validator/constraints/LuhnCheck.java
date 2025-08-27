package org.hibernate.validator.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/LuhnCheck.class */
public @interface LuhnCheck {

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/LuhnCheck$List.class */
    public @interface List {
        LuhnCheck[] value();
    }

    String message() default "{org.hibernate.validator.constraints.LuhnCheck.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int startIndex() default 0;

    int endIndex() default Integer.MAX_VALUE;

    int checkDigitIndex() default -1;

    boolean ignoreNonDigitCharacters() default true;
}
