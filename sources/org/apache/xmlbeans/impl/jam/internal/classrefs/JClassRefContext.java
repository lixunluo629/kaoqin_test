package org.apache.xmlbeans.impl.jam.internal.classrefs;

import org.apache.xmlbeans.impl.jam.JamClassLoader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/classrefs/JClassRefContext.class */
public interface JClassRefContext {
    String getPackageName();

    String[] getImportSpecs();

    JamClassLoader getClassLoader();
}
