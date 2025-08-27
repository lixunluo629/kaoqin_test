package org.hibernate.validator.internal.constraintvalidators.hv;

import java.util.List;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Messages;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

@SupportedValidationTarget({ValidationTarget.PARAMETERS})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/hv/ParameterScriptAssertValidator.class */
public class ParameterScriptAssertValidator implements ConstraintValidator<ParameterScriptAssert, Object[]> {
    private ScriptAssertContext scriptAssertContext;

    @Override // javax.validation.ConstraintValidator
    public void initialize(ParameterScriptAssert constraintAnnotation) {
        validateParameters(constraintAnnotation);
        this.scriptAssertContext = new ScriptAssertContext(constraintAnnotation.lang(), constraintAnnotation.script());
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Object[] arguments, ConstraintValidatorContext constraintValidatorContext) {
        List<String> parameterNames = ((ConstraintValidatorContextImpl) constraintValidatorContext).getMethodParameterNames();
        Map<String, Object> bindings = getBindings(arguments, parameterNames);
        return this.scriptAssertContext.evaluateScriptAssertExpression(bindings);
    }

    private Map<String, Object> getBindings(Object[] arguments, List<String> parameterNames) {
        Map<String, Object> bindings = CollectionHelper.newHashMap();
        for (int i = 0; i < arguments.length; i++) {
            bindings.put(parameterNames.get(i), arguments[i]);
        }
        return bindings;
    }

    private void validateParameters(ParameterScriptAssert constraintAnnotation) {
        Contracts.assertNotEmpty(constraintAnnotation.script(), Messages.MESSAGES.parameterMustNotBeEmpty("script"));
        Contracts.assertNotEmpty(constraintAnnotation.lang(), Messages.MESSAGES.parameterMustNotBeEmpty(AbstractHtmlElementTag.LANG_ATTRIBUTE));
    }
}
