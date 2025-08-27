package org.hibernate.validator.messageinterpolation;

import javax.validation.MessageInterpolator;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/messageinterpolation/HibernateMessageInterpolatorContext.class */
public interface HibernateMessageInterpolatorContext extends MessageInterpolator.Context {
    Class<?> getRootBeanType();
}
