package org.hibernate.validator.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/ConstraintComposition.class */
public @interface ConstraintComposition {
    CompositionType value() default CompositionType.AND;
}
