package org.springframework.web.servlet.support;

import java.beans.PropertyEditor;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.util.HtmlUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/support/BindStatus.class */
public class BindStatus {
    private final RequestContext requestContext;
    private final String path;
    private final boolean htmlEscape;
    private final String expression;
    private final Errors errors;
    private BindingResult bindingResult;
    private Object value;
    private Class<?> valueType;
    private Object actualValue;
    private PropertyEditor editor;
    private List<? extends ObjectError> objectErrors;
    private String[] errorCodes;
    private String[] errorMessages;

    public BindStatus(RequestContext requestContext, String path, boolean htmlEscape) throws IllegalStateException {
        String beanName;
        this.requestContext = requestContext;
        this.path = path;
        this.htmlEscape = htmlEscape;
        int dotPos = path.indexOf(46);
        if (dotPos == -1) {
            beanName = path;
            this.expression = null;
        } else {
            beanName = path.substring(0, dotPos);
            this.expression = path.substring(dotPos + 1);
        }
        this.errors = requestContext.getErrors(beanName, false);
        if (this.errors != null) {
            if (this.expression != null) {
                if ("*".equals(this.expression)) {
                    this.objectErrors = this.errors.getAllErrors();
                } else if (this.expression.endsWith("*")) {
                    this.objectErrors = this.errors.getFieldErrors(this.expression);
                } else {
                    this.objectErrors = this.errors.getFieldErrors(this.expression);
                    this.value = this.errors.getFieldValue(this.expression);
                    this.valueType = this.errors.getFieldType(this.expression);
                    if (this.errors instanceof BindingResult) {
                        this.bindingResult = (BindingResult) this.errors;
                        this.actualValue = this.bindingResult.getRawFieldValue(this.expression);
                        this.editor = this.bindingResult.findEditor(this.expression, null);
                    } else {
                        this.actualValue = this.value;
                    }
                }
            } else {
                this.objectErrors = this.errors.getGlobalErrors();
            }
            initErrorCodes();
        } else {
            Object target = requestContext.getModelObject(beanName);
            if (target == null) {
                throw new IllegalStateException("Neither BindingResult nor plain target object for bean name '" + beanName + "' available as request attribute");
            }
            if (this.expression != null && !"*".equals(this.expression) && !this.expression.endsWith("*")) {
                BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(target);
                this.value = bw.getPropertyValue(this.expression);
                this.valueType = bw.getPropertyType(this.expression);
                this.actualValue = this.value;
            }
            this.errorCodes = new String[0];
            this.errorMessages = new String[0];
        }
        if (htmlEscape && (this.value instanceof String)) {
            this.value = HtmlUtils.htmlEscape((String) this.value);
        }
    }

    private void initErrorCodes() {
        this.errorCodes = new String[this.objectErrors.size()];
        for (int i = 0; i < this.objectErrors.size(); i++) {
            ObjectError error = this.objectErrors.get(i);
            this.errorCodes[i] = error.getCode();
        }
    }

    private void initErrorMessages() throws NoSuchMessageException {
        if (this.errorMessages == null) {
            this.errorMessages = new String[this.objectErrors.size()];
            for (int i = 0; i < this.objectErrors.size(); i++) {
                ObjectError error = this.objectErrors.get(i);
                this.errorMessages[i] = this.requestContext.getMessage(error, this.htmlEscape);
            }
        }
    }

    public String getPath() {
        return this.path;
    }

    public String getExpression() {
        return this.expression;
    }

    public Object getValue() {
        return this.value;
    }

    public Class<?> getValueType() {
        return this.valueType;
    }

    public Object getActualValue() {
        return this.actualValue;
    }

    public String getDisplayValue() {
        if (this.value instanceof String) {
            return (String) this.value;
        }
        if (this.value != null) {
            return this.htmlEscape ? HtmlUtils.htmlEscape(this.value.toString()) : this.value.toString();
        }
        return "";
    }

    public boolean isError() {
        return this.errorCodes != null && this.errorCodes.length > 0;
    }

    public String[] getErrorCodes() {
        return this.errorCodes;
    }

    public String getErrorCode() {
        return this.errorCodes.length > 0 ? this.errorCodes[0] : "";
    }

    public String[] getErrorMessages() throws NoSuchMessageException {
        initErrorMessages();
        return this.errorMessages;
    }

    public String getErrorMessage() throws NoSuchMessageException {
        initErrorMessages();
        return this.errorMessages.length > 0 ? this.errorMessages[0] : "";
    }

    public String getErrorMessagesAsString(String delimiter) throws NoSuchMessageException {
        initErrorMessages();
        return StringUtils.arrayToDelimitedString(this.errorMessages, delimiter);
    }

    public Errors getErrors() {
        return this.errors;
    }

    public PropertyEditor getEditor() {
        return this.editor;
    }

    public PropertyEditor findEditor(Class<?> valueClass) {
        if (this.bindingResult != null) {
            return this.bindingResult.findEditor(this.expression, valueClass);
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("BindStatus: ");
        sb.append("expression=[").append(this.expression).append("]; ");
        sb.append("value=[").append(this.value).append("]");
        if (isError()) {
            sb.append("; errorCodes=").append(Arrays.asList(this.errorCodes));
        }
        return sb.toString();
    }
}
