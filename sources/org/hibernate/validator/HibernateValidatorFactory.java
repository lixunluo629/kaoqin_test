package org.hibernate.validator;

import javax.validation.ValidatorFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/HibernateValidatorFactory.class */
public interface HibernateValidatorFactory extends ValidatorFactory {
    @Override // javax.validation.ValidatorFactory
    HibernateValidatorContext usingContext();
}
