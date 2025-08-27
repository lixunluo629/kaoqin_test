package org.springframework.boot.diagnostics;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/diagnostics/FailureAnalyzer.class */
public interface FailureAnalyzer {
    FailureAnalysis analyze(Throwable th);
}
