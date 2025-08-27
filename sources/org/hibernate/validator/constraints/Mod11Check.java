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
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/Mod11Check.class */
public @interface Mod11Check {

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/Mod11Check$List.class */
    public @interface List {
        Mod11Check[] value();
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/Mod11Check$ProcessingDirection.class */
    public enum ProcessingDirection {
        RIGHT_TO_LEFT,
        LEFT_TO_RIGHT
    }

    String message() default "{org.hibernate.validator.constraints.Mod11Check.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int threshold() default Integer.MAX_VALUE;

    int startIndex() default 0;

    int endIndex() default Integer.MAX_VALUE;

    int checkDigitIndex() default -1;

    boolean ignoreNonDigitCharacters() default false;

    char treatCheck10As() default 'X';

    char treatCheck11As() default '0';

    ProcessingDirection processingDirection() default ProcessingDirection.RIGHT_TO_LEFT;
}
