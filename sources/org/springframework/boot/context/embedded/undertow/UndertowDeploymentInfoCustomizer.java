package org.springframework.boot.context.embedded.undertow;

import io.undertow.servlet.api.DeploymentInfo;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowDeploymentInfoCustomizer.class */
public interface UndertowDeploymentInfoCustomizer {
    void customize(DeploymentInfo deploymentInfo);
}
