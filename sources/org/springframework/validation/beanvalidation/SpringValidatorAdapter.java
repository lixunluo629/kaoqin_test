package org.springframework.validation.beanvalidation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/beanvalidation/SpringValidatorAdapter.class */
public class SpringValidatorAdapter implements SmartValidator, Validator {
    private static final Set<String> internalAnnotationAttributes = new HashSet(3);
    private Validator targetValidator;

    static {
        internalAnnotationAttributes.add(ConstraintHelper.MESSAGE);
        internalAnnotationAttributes.add(ConstraintHelper.GROUPS);
        internalAnnotationAttributes.add(ConstraintHelper.PAYLOAD);
    }

    public SpringValidatorAdapter(Validator targetValidator) {
        Assert.notNull(targetValidator, "Target Validator must not be null");
        this.targetValidator = targetValidator;
    }

    SpringValidatorAdapter() {
    }

    void setTargetValidator(Validator targetValidator) {
        this.targetValidator = targetValidator;
    }

    @Override // org.springframework.validation.Validator
    public boolean supports(Class<?> clazz) {
        return this.targetValidator != null;
    }

    @Override // org.springframework.validation.Validator
    public void validate(Object target, Errors errors) {
        if (this.targetValidator != null) {
            processConstraintViolations(this.targetValidator.validate(target, new Class[0]), errors);
        }
    }

    @Override // org.springframework.validation.SmartValidator
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (this.targetValidator != null) {
            Set<Class<?>> groups = new LinkedHashSet<>();
            if (validationHints != null) {
                for (Object hint : validationHints) {
                    if (hint instanceof Class) {
                        groups.add((Class) hint);
                    }
                }
            }
            processConstraintViolations(this.targetValidator.validate(target, ClassUtils.toClassArray(groups)), errors);
        }
    }

    protected void processConstraintViolations(Set<ConstraintViolation<Object>> violations, Errors errors) {
        for (ConstraintViolation<Object> violation : violations) {
            String field = determineField(violation);
            FieldError fieldError = errors.getFieldError(field);
            if (fieldError == null || !fieldError.isBindingFailure()) {
                try {
                    ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
                    String errorCode = determineErrorCode(cd);
                    Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
                    if (errors instanceof BindingResult) {
                        BindingResult bindingResult = (BindingResult) errors;
                        String nestedField = bindingResult.getNestedPath() + field;
                        if ("".equals(nestedField)) {
                            String[] errorCodes = bindingResult.resolveMessageCodes(errorCode);
                            bindingResult.addError(new ObjectError(errors.getObjectName(), errorCodes, errorArgs, violation.getMessage()));
                        } else {
                            Object rejectedValue = getRejectedValue(field, violation, bindingResult);
                            String[] errorCodes2 = bindingResult.resolveMessageCodes(errorCode, field);
                            bindingResult.addError(new FieldError(errors.getObjectName(), nestedField, rejectedValue, false, errorCodes2, errorArgs, violation.getMessage()));
                        }
                    } else {
                        errors.rejectValue(field, errorCode, errorArgs, violation.getMessage());
                    }
                } catch (NotReadablePropertyException ex) {
                    throw new IllegalStateException("JSR-303 validated property '" + field + "' does not have a corresponding accessor for Spring data binding - check your DataBinder's configuration (bean property versus direct field access)", ex);
                }
            }
        }
    }

    protected String determineField(ConstraintViolation<Object> violation) {
        String path = violation.getPropertyPath().toString();
        int elementIndex = path.indexOf(".<");
        return elementIndex >= 0 ? path.substring(0, elementIndex) : path;
    }

    protected String determineErrorCode(ConstraintDescriptor<?> descriptor) {
        return descriptor.getAnnotation().annotationType().getSimpleName();
    }

    protected Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor) {
        List<Object> arguments = new ArrayList<>();
        arguments.add(getResolvableField(objectName, field));
        Map<String, Object> attributesToExpose = new TreeMap<>();
        for (Map.Entry<String, Object> entry : descriptor.getAttributes().entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();
            if (!internalAnnotationAttributes.contains(attributeName)) {
                if (attributeValue instanceof String) {
                    attributeValue = new ResolvableAttribute(attributeValue.toString());
                }
                attributesToExpose.put(attributeName, attributeValue);
            }
        }
        arguments.addAll(attributesToExpose.values());
        return arguments.toArray();
    }

    protected MessageSourceResolvable getResolvableField(String objectName, String field) {
        String[] codes = {objectName + "." + field, field};
        return new DefaultMessageSourceResolvable(codes, field);
    }

    protected Object getRejectedValue(String field, ConstraintViolation<Object> violation, BindingResult bindingResult) {
        Object invalidValue = violation.getInvalidValue();
        if (!"".equals(field) && !field.contains("[]") && (invalidValue == violation.getLeafBean() || field.contains(PropertyAccessor.PROPERTY_KEY_PREFIX) || field.contains("."))) {
            invalidValue = bindingResult.getRawFieldValue(field);
        }
        return invalidValue;
    }

    @Override // javax.validation.Validator
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        Assert.state(this.targetValidator != null, "No target Validator set");
        return this.targetValidator.validate(object, groups);
    }

    @Override // javax.validation.Validator
    public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        Assert.state(this.targetValidator != null, "No target Validator set");
        return this.targetValidator.validateProperty(object, propertyName, groups);
    }

    @Override // javax.validation.Validator
    public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        Assert.state(this.targetValidator != null, "No target Validator set");
        return this.targetValidator.validateValue(beanType, propertyName, value, groups);
    }

    @Override // javax.validation.Validator
    public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        Assert.state(this.targetValidator != null, "No target Validator set");
        return this.targetValidator.getConstraintsForClass(clazz);
    }

    public <T> T unwrap(Class<T> cls) {
        Assert.state(this.targetValidator != null, "No target Validator set");
        try {
            return cls != null ? (T) this.targetValidator.unwrap(cls) : (T) this.targetValidator;
        } catch (ValidationException e) {
            if (Validator.class == cls) {
                return (T) this.targetValidator;
            }
            throw e;
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/beanvalidation/SpringValidatorAdapter$ResolvableAttribute.class */
    private static class ResolvableAttribute implements MessageSourceResolvable, Serializable {
        private final String resolvableString;

        public ResolvableAttribute(String resolvableString) {
            this.resolvableString = resolvableString;
        }

        @Override // org.springframework.context.MessageSourceResolvable
        public String[] getCodes() {
            return new String[]{this.resolvableString};
        }

        @Override // org.springframework.context.MessageSourceResolvable
        public Object[] getArguments() {
            return null;
        }

        @Override // org.springframework.context.MessageSourceResolvable
        public String getDefaultMessage() {
            return this.resolvableString;
        }
    }
}
