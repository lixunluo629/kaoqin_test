package org.bouncycastle.est.jcajce;

import java.io.IOException;
import javax.net.ssl.SSLSession;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/jcajce/JsseHostnameAuthorizer.class */
public interface JsseHostnameAuthorizer {
    boolean verified(String str, SSLSession sSLSession) throws IOException;
}
