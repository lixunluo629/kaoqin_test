package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/WeavingTransformer.class */
public class WeavingTransformer {
    private final ClassLoader classLoader;
    private final List<ClassFileTransformer> transformers = new ArrayList();

    public WeavingTransformer(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("ClassLoader must not be null");
        }
        this.classLoader = classLoader;
    }

    public void addTransformer(ClassFileTransformer transformer) {
        if (transformer == null) {
            throw new IllegalArgumentException("Transformer must not be null");
        }
        this.transformers.add(transformer);
    }

    public byte[] transformIfNecessary(String className, byte[] bytes) {
        String internalName = className.replace(".", "/");
        return transformIfNecessary(className, internalName, bytes, null);
    }

    public byte[] transformIfNecessary(String className, String internalName, byte[] bytes, ProtectionDomain pd) {
        byte[] result = bytes;
        for (ClassFileTransformer cft : this.transformers) {
            try {
                byte[] transformed = cft.transform(this.classLoader, internalName, (Class) null, pd, result);
                if (transformed != null) {
                    result = transformed;
                }
            } catch (IllegalClassFormatException ex) {
                throw new IllegalStateException("Class file transformation failed", ex);
            }
        }
        return result;
    }
}
