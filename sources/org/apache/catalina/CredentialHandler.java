package org.apache.catalina;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/CredentialHandler.class */
public interface CredentialHandler {
    boolean matches(String str, String str2);

    String mutate(String str);
}
