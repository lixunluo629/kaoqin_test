package org.apache.catalina.realm;

import java.security.cert.X509Certificate;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/realm/X509UsernameRetriever.class */
public interface X509UsernameRetriever {
    String getUsername(X509Certificate x509Certificate);
}
