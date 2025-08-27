package org.springframework.cglib.core;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/GeneratorStrategy.class */
public interface GeneratorStrategy {
    byte[] generate(ClassGenerator classGenerator) throws Exception;

    boolean equals(Object obj);
}
