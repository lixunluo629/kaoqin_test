package org.apache.catalina.realm;

import java.security.cert.X509Certificate;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/realm/X509SubjectDnRetriever.class */
public class X509SubjectDnRetriever implements X509UsernameRetriever {
    @Override // org.apache.catalina.realm.X509UsernameRetriever
    public String getUsername(X509Certificate clientCert) {
        return clientCert.getSubjectDN().getName();
    }
}
