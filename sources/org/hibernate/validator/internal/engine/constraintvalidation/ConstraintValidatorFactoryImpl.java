package org.hibernate.validator.internal.engine.constraintvalidation;

import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorFactoryImpl.class */
public class ConstraintValidatorFactoryImpl implements ConstraintValidatorFactory {
    @Override // javax.validation.ConstraintValidatorFactory
    public final <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return (T) run(NewInstance.action(key, "ConstraintValidator"));
    }

    @Override // javax.validation.ConstraintValidatorFactory
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
    }

    private <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
