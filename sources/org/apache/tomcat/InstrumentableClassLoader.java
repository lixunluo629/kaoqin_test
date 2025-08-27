package org.apache.tomcat;

import java.lang.instrument.ClassFileTransformer;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/InstrumentableClassLoader.class */
public interface InstrumentableClassLoader {
    void addTransformer(ClassFileTransformer classFileTransformer);

    void removeTransformer(ClassFileTransformer classFileTransformer);

    ClassLoader copyWithoutTransformers();
}
