package org.springframework.plugin.core;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/Plugin.class */
public interface Plugin<S> {
    boolean supports(S s);
}
