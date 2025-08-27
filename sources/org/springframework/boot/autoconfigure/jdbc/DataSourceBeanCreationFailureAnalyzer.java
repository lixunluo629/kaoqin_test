package org.springframework.boot.autoconfigure.jdbc;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceBeanCreationFailureAnalyzer.class */
class DataSourceBeanCreationFailureAnalyzer extends AbstractFailureAnalyzer<DataSourceProperties.DataSourceBeanCreationException> {
    DataSourceBeanCreationFailureAnalyzer() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.diagnostics.AbstractFailureAnalyzer
    public FailureAnalysis analyze(Throwable rootFailure, DataSourceProperties.DataSourceBeanCreationException cause) {
        String message = cause.getMessage();
        String description = message.substring(0, message.indexOf(".")).trim();
        String action = message.substring(message.indexOf(".") + 1).trim();
        return new FailureAnalysis(description, action, cause);
    }
}
