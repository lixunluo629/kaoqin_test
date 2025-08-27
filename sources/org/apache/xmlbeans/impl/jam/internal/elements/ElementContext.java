package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.annotation.AnnotationProxy;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/ElementContext.class */
public interface ElementContext extends JamLogger {
    JamLogger getLogger();

    JamClassLoader getClassLoader();

    AnnotationProxy createAnnotationProxy(String str);
}
