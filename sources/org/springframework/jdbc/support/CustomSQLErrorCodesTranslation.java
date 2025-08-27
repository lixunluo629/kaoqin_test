package org.springframework.jdbc.support;

import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/CustomSQLErrorCodesTranslation.class */
public class CustomSQLErrorCodesTranslation {
    private String[] errorCodes = new String[0];
    private Class<?> exceptionClass;

    public void setErrorCodes(String... errorCodes) {
        this.errorCodes = StringUtils.sortStringArray(errorCodes);
    }

    public String[] getErrorCodes() {
        return this.errorCodes;
    }

    public void setExceptionClass(Class<?> exceptionClass) {
        if (!DataAccessException.class.isAssignableFrom(exceptionClass)) {
            throw new IllegalArgumentException("Invalid exception class [" + exceptionClass + "]: needs to be a subclass of [org.springframework.dao.DataAccessException]");
        }
        this.exceptionClass = exceptionClass;
    }

    public Class<?> getExceptionClass() {
        return this.exceptionClass;
    }
}
