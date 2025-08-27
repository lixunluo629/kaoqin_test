package org.springframework.cglib.core;

import org.springframework.asm.Type;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/Customizer.class */
public interface Customizer extends KeyFactoryCustomizer {
    void customize(CodeEmitter codeEmitter, Type type);
}
