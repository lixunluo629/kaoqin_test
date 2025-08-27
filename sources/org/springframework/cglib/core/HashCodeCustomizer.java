package org.springframework.cglib.core;

import org.springframework.asm.Type;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/HashCodeCustomizer.class */
public interface HashCodeCustomizer extends KeyFactoryCustomizer {
    boolean customize(CodeEmitter codeEmitter, Type type);
}
