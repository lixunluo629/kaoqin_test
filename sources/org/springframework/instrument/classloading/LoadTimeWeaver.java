package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/LoadTimeWeaver.class */
public interface LoadTimeWeaver {
    void addTransformer(ClassFileTransformer classFileTransformer);

    ClassLoader getInstrumentableClassLoader();

    ClassLoader getThrowawayClassLoader();
}
