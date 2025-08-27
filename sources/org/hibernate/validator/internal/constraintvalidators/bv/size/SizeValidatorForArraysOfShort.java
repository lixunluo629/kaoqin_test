package org.hibernate.validator.internal.constraintvalidators.bv.size;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/size/SizeValidatorForArraysOfShort.class */
public class SizeValidatorForArraysOfShort extends SizeValidatorForArraysOfPrimitives implements ConstraintValidator<Size, short[]> {
    @Override // javax.validation.ConstraintValidator
    public /* bridge */ /* synthetic */ void initialize(Annotation annotation) {
        super.initialize((Size) annotation);
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(short[] array, ConstraintValidatorContext constraintValidatorContext) {
        if (array == null) {
            return true;
        }
        int length = Array.getLength(array);
        return length >= this.min && length <= this.max;
    }
}
