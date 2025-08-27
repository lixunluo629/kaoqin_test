package net.sf.cglib.core;

import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/ClassGenerator.class */
public interface ClassGenerator {
    void generateClass(ClassVisitor classVisitor) throws Exception;
}
