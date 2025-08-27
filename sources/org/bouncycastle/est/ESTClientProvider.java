package org.bouncycastle.est;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/ESTClientProvider.class */
public interface ESTClientProvider {
    ESTClient makeClient() throws ESTException;

    boolean isTrusted();
}
