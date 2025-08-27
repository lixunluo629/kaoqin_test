package net.sf.cglib.core;

import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/Customizer.class */
public interface Customizer {
    void customize(CodeEmitter codeEmitter, Type type);
}
