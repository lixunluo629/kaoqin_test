package org.apache.xmlbeans.impl.jam;

import java.io.File;
import java.io.PrintWriter;
import org.apache.xmlbeans.impl.jam.annotation.JavadocTagParser;
import org.apache.xmlbeans.impl.jam.provider.JamClassBuilder;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/JamServiceParams.class */
public interface JamServiceParams {
    void includeSourceFile(File file);

    void includeSourcePattern(File[] fileArr, String str);

    void excludeSourcePattern(File[] fileArr, String str);

    void includeClassPattern(File[] fileArr, String str);

    void excludeClassPattern(File[] fileArr, String str);

    void includeSourceFile(File[] fileArr, File file);

    void excludeSourceFile(File[] fileArr, File file);

    void includeClassFile(File[] fileArr, File file);

    void excludeClassFile(File[] fileArr, File file);

    void includeClass(String str);

    void excludeClass(String str);

    void addSourcepath(File file);

    void addClasspath(File file);

    void setLoggerWriter(PrintWriter printWriter);

    void setJamLogger(JamLogger jamLogger);

    void setVerbose(Class cls);

    void setShowWarnings(boolean z);

    void setParentClassLoader(JamClassLoader jamClassLoader);

    void addToolClasspath(File file);

    void setPropertyInitializer(MVisitor mVisitor);

    void addInitializer(MVisitor mVisitor);

    void setJavadocTagParser(JavadocTagParser javadocTagParser);

    void setUseSystemClasspath(boolean z);

    void addClassBuilder(JamClassBuilder jamClassBuilder);

    void addClassLoader(ClassLoader classLoader);

    void setProperty(String str, String str2);

    void set14WarningsEnabled(boolean z);

    void setVerbose(boolean z);
}
