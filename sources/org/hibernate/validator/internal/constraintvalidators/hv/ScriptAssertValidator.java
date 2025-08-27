package org.hibernate.validator.internal.constraintvalidators.hv;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraints.ScriptAssert;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Messages;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/hv/ScriptAssertValidator.class */
public class ScriptAssertValidator implements ConstraintValidator<ScriptAssert, Object> {
    private String alias;
    private ScriptAssertContext scriptAssertContext;

    @Override // javax.validation.ConstraintValidator
    public void initialize(ScriptAssert constraintAnnotation) {
        validateParameters(constraintAnnotation);
        this.alias = constraintAnnotation.alias();
        this.scriptAssertContext = new ScriptAssertContext(constraintAnnotation.lang(), constraintAnnotation.script());
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        return this.scriptAssertContext.evaluateScriptAssertExpression(value, this.alias);
    }

    private void validateParameters(ScriptAssert constraintAnnotation) {
        Contracts.assertNotEmpty(constraintAnnotation.script(), Messages.MESSAGES.parameterMustNotBeEmpty("script"));
        Contracts.assertNotEmpty(constraintAnnotation.lang(), Messages.MESSAGES.parameterMustNotBeEmpty(AbstractHtmlElementTag.LANG_ATTRIBUTE));
        Contracts.assertNotEmpty(constraintAnnotation.alias(), Messages.MESSAGES.parameterMustNotBeEmpty("alias"));
    }
}
