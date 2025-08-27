package org.springframework.util;

import java.util.UUID;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/JdkIdGenerator.class */
public class JdkIdGenerator implements IdGenerator {
    @Override // org.springframework.util.IdGenerator
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
