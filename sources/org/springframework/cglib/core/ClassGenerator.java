package org.springframework.cglib.core;

import org.springframework.asm.ClassVisitor;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/ClassGenerator.class */
public interface ClassGenerator {
    void generateClass(ClassVisitor classVisitor) throws Exception;
}
