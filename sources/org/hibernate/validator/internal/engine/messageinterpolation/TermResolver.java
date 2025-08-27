package org.hibernate.validator.internal.engine.messageinterpolation;

import javax.validation.MessageInterpolator;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/TermResolver.class */
public interface TermResolver {
    String interpolate(MessageInterpolator.Context context, String str);
}
