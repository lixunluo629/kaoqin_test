package org.springframework.validation;

import java.io.Serializable;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/AbstractErrors.class */
public abstract class AbstractErrors implements Errors, Serializable {
    private String nestedPath = "";
    private final Stack<String> nestedPathStack = new Stack<>();

    @Override // org.springframework.validation.Errors
    public void setNestedPath(String nestedPath) {
        doSetNestedPath(nestedPath);
        this.nestedPathStack.clear();
    }

    @Override // org.springframework.validation.Errors
    public String getNestedPath() {
        return this.nestedPath;
    }

    @Override // org.springframework.validation.Errors
    public void pushNestedPath(String subPath) {
        this.nestedPathStack.push(getNestedPath());
        doSetNestedPath(getNestedPath() + subPath);
    }

    @Override // org.springframework.validation.Errors
    public void popNestedPath() throws IllegalArgumentException {
        try {
            String formerNestedPath = this.nestedPathStack.pop();
            doSetNestedPath(formerNestedPath);
        } catch (EmptyStackException e) {
            throw new IllegalStateException("Cannot pop nested path: no nested path on stack");
        }
    }

    protected void doSetNestedPath(String nestedPath) {
        if (nestedPath == null) {
            nestedPath = "";
        }
        String nestedPath2 = canonicalFieldName(nestedPath);
        if (nestedPath2.length() > 0 && !nestedPath2.endsWith(".")) {
            nestedPath2 = nestedPath2 + ".";
        }
        this.nestedPath = nestedPath2;
    }

    protected String fixedField(String field) {
        if (StringUtils.hasLength(field)) {
            return getNestedPath() + canonicalFieldName(field);
        }
        String path = getNestedPath();
        return path.endsWith(".") ? path.substring(0, path.length() - ".".length()) : path;
    }

    protected String canonicalFieldName(String field) {
        return field;
    }

    @Override // org.springframework.validation.Errors
    public void reject(String errorCode) {
        reject(errorCode, null, null);
    }

    @Override // org.springframework.validation.Errors
    public void reject(String errorCode, String defaultMessage) {
        reject(errorCode, null, defaultMessage);
    }

    @Override // org.springframework.validation.Errors
    public void rejectValue(String field, String errorCode) {
        rejectValue(field, errorCode, null, null);
    }

    @Override // org.springframework.validation.Errors
    public void rejectValue(String field, String errorCode, String defaultMessage) {
        rejectValue(field, errorCode, null, defaultMessage);
    }

    @Override // org.springframework.validation.Errors
    public boolean hasErrors() {
        return !getAllErrors().isEmpty();
    }

    @Override // org.springframework.validation.Errors
    public int getErrorCount() {
        return getAllErrors().size();
    }

    @Override // org.springframework.validation.Errors
    public List<ObjectError> getAllErrors() {
        List<ObjectError> result = new LinkedList<>();
        result.addAll(getGlobalErrors());
        result.addAll(getFieldErrors());
        return Collections.unmodifiableList(result);
    }

    @Override // org.springframework.validation.Errors
    public boolean hasGlobalErrors() {
        return getGlobalErrorCount() > 0;
    }

    @Override // org.springframework.validation.Errors
    public int getGlobalErrorCount() {
        return getGlobalErrors().size();
    }

    @Override // org.springframework.validation.Errors
    public ObjectError getGlobalError() {
        List<ObjectError> globalErrors = getGlobalErrors();
        if (globalErrors.isEmpty()) {
            return null;
        }
        return globalErrors.get(0);
    }

    @Override // org.springframework.validation.Errors
    public boolean hasFieldErrors() {
        return getFieldErrorCount() > 0;
    }

    @Override // org.springframework.validation.Errors
    public int getFieldErrorCount() {
        return getFieldErrors().size();
    }

    @Override // org.springframework.validation.Errors
    public FieldError getFieldError() {
        List<FieldError> fieldErrors = getFieldErrors();
        if (fieldErrors.isEmpty()) {
            return null;
        }
        return fieldErrors.get(0);
    }

    @Override // org.springframework.validation.Errors
    public boolean hasFieldErrors(String field) {
        return getFieldErrorCount(field) > 0;
    }

    @Override // org.springframework.validation.Errors
    public int getFieldErrorCount(String field) {
        return getFieldErrors(field).size();
    }

    @Override // org.springframework.validation.Errors
    public List<FieldError> getFieldErrors(String field) {
        List<FieldError> fieldErrors = getFieldErrors();
        List<FieldError> result = new LinkedList<>();
        String fixedField = fixedField(field);
        for (FieldError error : fieldErrors) {
            if (isMatchingFieldError(fixedField, error)) {
                result.add(error);
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override // org.springframework.validation.Errors
    public FieldError getFieldError(String field) {
        List<FieldError> fieldErrors = getFieldErrors(field);
        if (fieldErrors.isEmpty()) {
            return null;
        }
        return fieldErrors.get(0);
    }

    @Override // org.springframework.validation.Errors
    public Class<?> getFieldType(String field) {
        Object value = getFieldValue(field);
        if (value != null) {
            return value.getClass();
        }
        return null;
    }

    protected boolean isMatchingFieldError(String field, FieldError fieldError) {
        if (field.equals(fieldError.getField())) {
            return true;
        }
        int endIndex = field.length() - 1;
        return endIndex >= 0 && field.charAt(endIndex) == '*' && (endIndex == 0 || field.regionMatches(0, fieldError.getField(), 0, endIndex));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(": ").append(getErrorCount()).append(" errors");
        for (ObjectError error : getAllErrors()) {
            sb.append('\n').append(error);
        }
        return sb.toString();
    }
}
