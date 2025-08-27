package org.springframework.core;

@Deprecated
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ControlFlow.class */
public interface ControlFlow {
    boolean under(Class<?> cls);

    boolean under(Class<?> cls, String str);

    boolean underToken(String str);
}
