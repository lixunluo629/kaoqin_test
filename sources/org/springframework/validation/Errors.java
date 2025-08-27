package org.springframework.validation;

import java.util.List;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/Errors.class */
public interface Errors {
    public static final String NESTED_PATH_SEPARATOR = ".";

    String getObjectName();

    void setNestedPath(String str);

    String getNestedPath();

    void pushNestedPath(String str);

    void popNestedPath() throws IllegalStateException;

    void reject(String str);

    void reject(String str, String str2);

    void reject(String str, Object[] objArr, String str2);

    void rejectValue(String str, String str2);

    void rejectValue(String str, String str2, String str3);

    void rejectValue(String str, String str2, Object[] objArr, String str3);

    void addAllErrors(Errors errors);

    boolean hasErrors();

    int getErrorCount();

    List<ObjectError> getAllErrors();

    boolean hasGlobalErrors();

    int getGlobalErrorCount();

    List<ObjectError> getGlobalErrors();

    ObjectError getGlobalError();

    boolean hasFieldErrors();

    int getFieldErrorCount();

    List<FieldError> getFieldErrors();

    FieldError getFieldError();

    boolean hasFieldErrors(String str);

    int getFieldErrorCount(String str);

    List<FieldError> getFieldErrors(String str);

    FieldError getFieldError(String str);

    Object getFieldValue(String str);

    Class<?> getFieldType(String str);
}
