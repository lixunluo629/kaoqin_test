package org.apache.xmlbeans.impl.jam.provider;

import java.io.File;
import java.io.IOException;
import org.apache.xmlbeans.impl.jam.annotation.JavadocTagParser;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/provider/JamServiceContext.class */
public interface JamServiceContext extends JamLogger {
    ResourcePath getInputClasspath();

    ResourcePath getInputSourcepath();

    ResourcePath getToolClasspath();

    String getProperty(String str);

    MVisitor getInitializer();

    ClassLoader[] getReflectionClassLoaders();

    File[] getSourceFiles() throws IOException;

    String[] getAllClassnames() throws IOException;

    JamLogger getLogger();

    JamClassBuilder getBaseBuilder();

    JavadocTagParser getTagParser();

    boolean is14WarningsEnabled();
}
