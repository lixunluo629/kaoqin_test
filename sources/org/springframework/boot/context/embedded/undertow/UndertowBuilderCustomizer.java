package org.springframework.boot.context.embedded.undertow;

import io.undertow.Undertow;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowBuilderCustomizer.class */
public interface UndertowBuilderCustomizer {
    void customize(Undertow.Builder builder);
}
