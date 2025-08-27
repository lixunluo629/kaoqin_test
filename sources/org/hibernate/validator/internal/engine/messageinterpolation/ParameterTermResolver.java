package org.hibernate.validator.internal.engine.messageinterpolation;

import java.util.Arrays;
import javax.validation.MessageInterpolator;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/ParameterTermResolver.class */
public class ParameterTermResolver implements TermResolver {
    @Override // org.hibernate.validator.internal.engine.messageinterpolation.TermResolver
    public String interpolate(MessageInterpolator.Context context, String expression) {
        String resolvedExpression;
        Object variable = context.getConstraintDescriptor().getAttributes().get(removeCurlyBraces(expression));
        if (variable != null) {
            if (variable.getClass().isArray()) {
                resolvedExpression = Arrays.toString((Object[]) variable);
            } else {
                resolvedExpression = variable.toString();
            }
        } else {
            resolvedExpression = expression;
        }
        return resolvedExpression;
    }

    private String removeCurlyBraces(String parameter) {
        return parameter.substring(1, parameter.length() - 1);
    }
}
