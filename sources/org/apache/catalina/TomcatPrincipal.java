package org.apache.catalina;

import java.security.Principal;
import org.ietf.jgss.GSSCredential;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/TomcatPrincipal.class */
public interface TomcatPrincipal extends Principal {
    Principal getUserPrincipal();

    GSSCredential getGssCredential();

    void logout() throws Exception;
}
