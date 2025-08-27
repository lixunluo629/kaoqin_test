package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;
import org.springframework.core.OverridingClassLoader;
import org.springframework.lang.UsesJava7;

@UsesJava7
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/SimpleInstrumentableClassLoader.class */
public class SimpleInstrumentableClassLoader extends OverridingClassLoader {
    private final WeavingTransformer weavingTransformer;

    static {
        if (parallelCapableClassLoaderAvailable) {
            ClassLoader.registerAsParallelCapable();
        }
    }

    public SimpleInstrumentableClassLoader(ClassLoader parent) {
        super(parent);
        this.weavingTransformer = new WeavingTransformer(parent);
    }

    public void addTransformer(ClassFileTransformer transformer) {
        this.weavingTransformer.addTransformer(transformer);
    }

    @Override // org.springframework.core.OverridingClassLoader
    protected byte[] transformIfNecessary(String name, byte[] bytes) {
        return this.weavingTransformer.transformIfNecessary(name, bytes);
    }
}
