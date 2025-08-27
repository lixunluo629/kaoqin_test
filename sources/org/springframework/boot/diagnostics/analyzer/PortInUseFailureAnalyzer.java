package org.springframework.boot.diagnostics.analyzer;

import org.springframework.boot.context.embedded.PortInUseException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/diagnostics/analyzer/PortInUseFailureAnalyzer.class */
class PortInUseFailureAnalyzer extends AbstractFailureAnalyzer<PortInUseException> {
    PortInUseFailureAnalyzer() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.diagnostics.AbstractFailureAnalyzer
    public FailureAnalysis analyze(Throwable rootFailure, PortInUseException cause) {
        return new FailureAnalysis("Embedded servlet container failed to start. Port " + cause.getPort() + " was already in use.", "Identify and stop the process that's listening on port " + cause.getPort() + " or configure this application to listen on another port.", cause);
    }
}
