package org.apache.xmlbeans.impl.jam;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JamService.class */
public interface JamService {
    JamClassLoader getClassLoader();

    String[] getClassNames();

    JamClassIterator getClasses();

    JClass[] getAllClasses();
}
