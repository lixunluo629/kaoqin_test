package org.apache.catalina.servlet4preview;

import javax.servlet.ServletRegistration;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlet4preview/ServletContext.class */
public interface ServletContext extends javax.servlet.ServletContext {
    int getSessionTimeout();

    void setSessionTimeout(int i);

    ServletRegistration.Dynamic addJspFile(String str, String str2);

    String getRequestCharacterEncoding();

    void setRequestCharacterEncoding(String str);

    String getResponseCharacterEncoding();

    void setResponseCharacterEncoding(String str);
}
