package org.hibernate.validator.internal.engine.messageinterpolation;

import java.util.Locale;
import java.util.Map;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import javax.validation.MessageInterpolator;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.internal.engine.messageinterpolation.el.RootResolver;
import org.hibernate.validator.internal.engine.messageinterpolation.el.SimpleELContext;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/ElTermResolver.class */
public class ElTermResolver implements TermResolver {
    private static final Log log = LoggerFactory.make();
    private static final String VALIDATED_VALUE_NAME = "validatedValue";
    private final Locale locale;
    private final ExpressionFactory expressionFactory;

    public ElTermResolver(Locale locale, ExpressionFactory expressionFactory) {
        this.locale = locale;
        this.expressionFactory = expressionFactory;
    }

    @Override // org.hibernate.validator.internal.engine.messageinterpolation.TermResolver
    public String interpolate(MessageInterpolator.Context context, String expression) {
        String resolvedExpression = expression;
        SimpleELContext elContext = new SimpleELContext(this.expressionFactory);
        try {
            ValueExpression valueExpression = bindContextValues(expression, context, elContext);
            resolvedExpression = (String) valueExpression.getValue(elContext);
        } catch (PropertyNotFoundException pnfe) {
            log.unknownPropertyInExpressionLanguage(expression, pnfe);
        } catch (ELException e) {
            log.errorInExpressionLanguage(expression, e);
        } catch (Exception e2) {
            log.evaluatingExpressionLanguageExpressionCausedException(expression, e2);
        }
        return resolvedExpression;
    }

    private ValueExpression bindContextValues(String messageTemplate, MessageInterpolator.Context messageInterpolatorContext, SimpleELContext elContext) {
        ValueExpression valueExpression = this.expressionFactory.createValueExpression(messageInterpolatorContext.getValidatedValue(), Object.class);
        elContext.setVariable(VALIDATED_VALUE_NAME, valueExpression);
        ValueExpression valueExpression2 = this.expressionFactory.createValueExpression(new FormatterWrapper(this.locale), FormatterWrapper.class);
        elContext.setVariable(RootResolver.FORMATTER, valueExpression2);
        for (Map.Entry<String, Object> entry : messageInterpolatorContext.getConstraintDescriptor().getAttributes().entrySet()) {
            ValueExpression valueExpression3 = this.expressionFactory.createValueExpression(entry.getValue(), Object.class);
            elContext.setVariable(entry.getKey(), valueExpression3);
        }
        if (messageInterpolatorContext instanceof MessageInterpolatorContext) {
            MessageInterpolatorContext internalContext = (MessageInterpolatorContext) messageInterpolatorContext;
            for (Map.Entry<String, Object> entry2 : internalContext.getMessageParameters().entrySet()) {
                ValueExpression valueExpression4 = this.expressionFactory.createValueExpression(entry2.getValue(), Object.class);
                elContext.setVariable(entry2.getKey(), valueExpression4);
            }
        }
        return this.expressionFactory.createValueExpression(elContext, messageTemplate, String.class);
    }
}
