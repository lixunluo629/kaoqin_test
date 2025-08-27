package org.apache.xmlbeans.impl.jam.provider;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/provider/JamLogger.class */
public interface JamLogger {
    void setVerbose(Class cls);

    boolean isVerbose(Object obj);

    boolean isVerbose(Class cls);

    void setShowWarnings(boolean z);

    void verbose(String str, Object obj);

    void verbose(Throwable th, Object obj);

    void verbose(String str);

    void verbose(Throwable th);

    void warning(Throwable th);

    void warning(String str);

    void error(Throwable th);

    void error(String str);

    boolean isVerbose();
}
