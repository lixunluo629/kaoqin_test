package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/jboss/JBossClassLoaderAdapter.class */
interface JBossClassLoaderAdapter {
    void addTransformer(ClassFileTransformer classFileTransformer);

    ClassLoader getInstrumentableClassLoader();
}
