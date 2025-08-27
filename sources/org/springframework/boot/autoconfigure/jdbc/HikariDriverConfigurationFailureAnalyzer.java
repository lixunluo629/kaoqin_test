package org.springframework.boot.autoconfigure.jdbc;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/HikariDriverConfigurationFailureAnalyzer.class */
class HikariDriverConfigurationFailureAnalyzer extends AbstractFailureAnalyzer<IllegalStateException> {
    private static final String EXPECTED_MESSAGE = "both driverClassName and dataSourceClassName are specified, one or the other should be used";

    HikariDriverConfigurationFailureAnalyzer() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.diagnostics.AbstractFailureAnalyzer
    public FailureAnalysis analyze(Throwable rootFailure, IllegalStateException cause) {
        if (!EXPECTED_MESSAGE.equals(cause.getMessage())) {
            return null;
        }
        return new FailureAnalysis("Configuration of the Hikari connection pool failed: 'dataSourceClassName' is not supported.", "Spring Boot auto-configures only a driver and can't specify a custom DataSource. Consider configuring the Hikari DataSource in your own configuration.", cause);
    }
}
