package org.apache.catalina.ssi;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/ssi/SSIExternalResolver.class */
public interface SSIExternalResolver {
    void addVariableNames(Collection<String> collection);

    String getVariableValue(String str);

    void setVariableValue(String str, String str2);

    Date getCurrentDate();

    long getFileSize(String str, boolean z) throws IOException;

    long getFileLastModified(String str, boolean z) throws IOException;

    String getFileText(String str, boolean z) throws IOException;

    void log(String str, Throwable th);
}
