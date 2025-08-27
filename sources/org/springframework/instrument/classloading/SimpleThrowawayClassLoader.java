package org.springframework.instrument.classloading;

import org.springframework.core.OverridingClassLoader;
import org.springframework.lang.UsesJava7;

@UsesJava7
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/SimpleThrowawayClassLoader.class */
public class SimpleThrowawayClassLoader extends OverridingClassLoader {
    static {
        if (parallelCapableClassLoaderAvailable) {
            ClassLoader.registerAsParallelCapable();
        }
    }

    public SimpleThrowawayClassLoader(ClassLoader parent) {
        super(parent);
    }
}
