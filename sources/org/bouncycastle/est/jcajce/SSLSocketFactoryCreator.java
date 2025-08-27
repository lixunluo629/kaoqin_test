package org.bouncycastle.est.jcajce;

import javax.net.ssl.SSLSocketFactory;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/jcajce/SSLSocketFactoryCreator.class */
public interface SSLSocketFactoryCreator {
    SSLSocketFactory createFactory() throws Exception;

    boolean isTrusted();
}
