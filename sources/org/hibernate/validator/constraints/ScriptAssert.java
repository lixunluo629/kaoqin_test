package org.hibernate.validator.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/ScriptAssert.class */
public @interface ScriptAssert {

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/ScriptAssert$List.class */
    public @interface List {
        ScriptAssert[] value();
    }

    String message() default "{org.hibernate.validator.constraints.ScriptAssert.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String lang();

    String script();

    String alias() default "_this";
}
