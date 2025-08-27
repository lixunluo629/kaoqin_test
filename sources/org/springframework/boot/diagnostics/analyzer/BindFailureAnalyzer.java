package org.springframework.boot.diagnostics.analyzer;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/diagnostics/analyzer/BindFailureAnalyzer.class */
class BindFailureAnalyzer extends AbstractFailureAnalyzer<BindException> {
    BindFailureAnalyzer() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.diagnostics.AbstractFailureAnalyzer
    public FailureAnalysis analyze(Throwable rootFailure, BindException cause) {
        if (CollectionUtils.isEmpty(cause.getAllErrors())) {
            return null;
        }
        StringBuilder description = new StringBuilder(String.format("Binding to target %s failed:%n", cause.getTarget()));
        for (ObjectError error : cause.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                description.append(String.format("%n    Property: %s", cause.getObjectName() + "." + fieldError.getField()));
                description.append(String.format("%n    Value: %s", fieldError.getRejectedValue()));
            }
            description.append(String.format("%n    Reason: %s%n", error.getDefaultMessage()));
        }
        return new FailureAnalysis(description.toString(), "Update your application's configuration", cause);
    }
}
