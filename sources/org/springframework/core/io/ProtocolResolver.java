package org.springframework.core.io;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/io/ProtocolResolver.class */
public interface ProtocolResolver {
    Resource resolve(String str, ResourceLoader resourceLoader);
}
