package org.bouncycastle.est;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/TLSUniqueProvider.class */
public interface TLSUniqueProvider {
    boolean isTLSUniqueAvailable();

    byte[] getTLSUnique();
}
