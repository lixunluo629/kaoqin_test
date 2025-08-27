package org.hibernate.validator.messageinterpolation;

import java.util.Locale;
import javax.validation.MessageInterpolator;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
import org.hibernate.validator.internal.engine.messageinterpolation.ParameterTermResolver;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/messageinterpolation/ParameterMessageInterpolator.class */
public class ParameterMessageInterpolator extends AbstractMessageInterpolator {
    private static final Log log = LoggerFactory.make();

    public ParameterMessageInterpolator() {
        log.creationOfParameterMessageInterpolation();
    }

    @Override // org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator
    public String interpolate(MessageInterpolator.Context context, Locale locale, String term) {
        if (InterpolationTerm.isElExpression(term)) {
            log.getElUnsupported(term);
            return term;
        }
        ParameterTermResolver parameterTermResolver = new ParameterTermResolver();
        return parameterTermResolver.interpolate(context, term);
    }
}
